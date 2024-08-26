with open("doctors_aid_inputs.txt", "a", encoding="utf-8") as file:
    file.write("\n")

def read_inputtxt(): #function to read doctors_aid_inputs.txt file
    char="a"
    inputs_lists=[]
    with open("doctors_aid_inputs.txt", "r", encoding = "utf-8") as f:
        while char!="":
            char=f.readline()
            inputs_lists.append(char)
        f.close()
        return inputs_lists

def write_output(input): #function to add outputs to doctors_aid_outputs.txt file
    with open("doctors_aid_outputs.txt", "a", encoding = "utf-8") as f:
        f.write(input)
        f.close()    

def create(): #function to create patient profile
    patient_name= line.split(", ")[0].split()[1]
    duplicate_found = False
    if len(patient_list) > 0:
        for y in range (len(patient_list)): 
            if patient_name in patient_list[y]:
                write_output("Patient "+patient_name+ " cannot be recorded due to duplication.\n")
                duplicate_found = True
        if not duplicate_found:   
            patient=[]
            patient.append(patient_name)
            for i in range (1,6):
                patient.append(line.split(", ")[i])
            patient_list.append(patient)
            write_output("Patient "+patient_name+ " is recorded.\n")
    else:
        patient=[]
        patient.append(patient_name)
        for i in range (1,6):
            patient.append(line.split(", ")[i])
        patient_list.append(patient)
        write_output("Patient "+patient_name+ " is recorded.\n")

def remove(patient_name): #function to remove patient from patient list
    is_removed = False
    for i in range(len(patient_list)):
        if patient_list[i][0] == patient_name:
            is_removed = True
            patient_list.pop(i)
            write_output("Patient "+ patient_name + " is removed.\n")  
            break
    if is_removed == False:
        write_output("Patient "+patient_name+ " cannot be removed due to absence.\n") 
        
      
def probability(): #function to calculate patient's probability of having disease.
    patient_name= line.split(", ")[0].split()[1]
    absence_patient= False
    for i in range (len(patient_list)):
        if  patient_list[i][0] == patient_name:           
            absence_patient= True
            diagnosis_accuracy=float(patient_list[i][1])
            disease_incidence=patient_list[i][3]
            upper=float(disease_incidence[0:2])
            lower=float(disease_incidence[3:])
            global patient_probability
            patient_probability=100 * upper/((lower-upper)*(1-diagnosis_accuracy)+upper)
            patient_probability=round(patient_probability,2)
            write_output(f"Patient {patient_name} has a probability of {patient_probability}% of having {patient_list[i][2]}\n")
    if absence_patient == False:
        write_output(f"Probability for {patient_name} cannot be calculated due to absence.\n") 

def recommendation(): #function to recommend whether patient should have the treatment or not
    patient_name= line.split(", ")[0].split()[1]
    absence_recommendation= False
    for i in range (len(patient_list)):
        if  patient_list[i][0] == patient_name:
            absence_recommendation= True
            treatment_risk = patient_list[i][5].replace("\n", "")
            if float(patient_probability) > float(treatment_risk)*100:
                write_output("System suggests "+ patient_name + " to have the treatment."+ "\n")
            else:
                write_output("System suggests "+ patient_name + " NOT to have the treatment."+ "\n")
    if absence_recommendation== False:
        write_output(f"Recommendation for {patient_name} cannot be calculated due to absence.\n")


def listt(): #function to show patient profile as a list
    first_line = f'{"Patient": <8}{"Diagnosis": <16}{"Disease": <16}{"Disease": <12}{"Treatment": <16}{"Treatment"}\n'
    second_line = f'{"Name": <8}{"Accuracy": <16}{"Name": <16}{"Incidence": <12}{"Name": <16}{"Risk"}\n'
    third_line = "-------------------------------------------------------------------------\n"
    write_output(first_line)
    write_output(second_line)
    write_output(third_line)
    for x in range(len(patient_list)):
        number1 = str(float(patient_list[x][1]) * 100) + "%"
        number2 = (patient_list[x][5])
        number2 = number2[0:-1]
        number2 = float(number2) * 100
        c = f'{patient_list[x][0]:<8}{number1:<16}{patient_list[x][2]:<16}{patient_list[x][3]:<12}{patient_list[x][4]:<16}{number2}%\n'
        write_output(c)


data= read_inputtxt()
patient_list=list(list())
for line in data:
    
    command1=line.split(", ")
    if len(command1)!=1:
        create()
    else:
        try:
            command=line.split()[0]
            if command =="remove":
                patient_name= line.split()[1] 
                remove(patient_name)
            elif command =="list":
                listt()
            elif command =="probability":
                probability()
            else:
                recommendation()

        except:
            pass
            
            
#AYKUT ALP GÜRLER
#b2210356024