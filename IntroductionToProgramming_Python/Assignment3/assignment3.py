import sys

categories = {}

def lettoint(letter):
	res = {
	"A" : 0,
	"B" : 1,
	"C" : 2,
	"D" : 3,
	"E" : 4,
	"F" : 5,
	"G" : 6,
	"H" : 7,
	"I" : 8,
	"J" : 9,
	"K" : 10,
	"L" : 11,
	"M" : 12,
	"N" : 13,
	"O" : 14,
	"P" : 15,
	"Q" : 16,
	"R" : 17,
	"S" : 18,
	"T" : 19,
	"U" : 20,
	"V" : 21,
	"W" : 22,
	"X" : 23,
	"Y" : 24,
	"Z" : 25
		}

	return res[letter]

def inttolet(integer):
	res = {
	0 : "A",
	1 : "B",
	2 : "C",
	3 : "D",
	4 : "E",
	5 : "F",
	6 : "G",
	7 : "H",
	8 : "I",
	9 : "J",
	10: "K",
	11: "L",
	12: "M",
	13: "N",
	14: "O",
	15: "P",
	16: "Q",
	17: "R",
	18: "S",
	19: "T",
	20: "U",
	21: "V",
	22: "W",
	23: "X",
	24: "Y",
	25: "Z"
	}
	return res[integer]


def create_category(name,row,column):
	if(name in categories):
		print("Warning: cannot create the category for the second time. The stadium has already {0}.".format(name))

	else:
		listemiz =[]
		for i in range(row):
			ll = []
			for j in range(column):
				ll.append("X")
			listemiz.append(ll)

		categories[name] = {"student":0,"full":0,"season":0,"total" : 0 , "layout" : listemiz}
		print("The category '{0}' having {1} seats has been created.".format(name,row*column))

def print_category(name):
	if(name in categories):
		print("Printing category layout of {0}".format(name))
		lay = categories[name]["layout"]
		for i in range(len(lay)-1,-1,-1):
			print(inttolet(i),end= " ")
			for x in lay[i]:
				print( x + " ",end = " ") 
			print ()
		print (" ",end = " ")
		for i in range(len(lay[0])):
			if(i < 10):
				print (str(i) + " ",end = " ")
			else:
				print (i ,end = " ") 
		print()

	else:
		print("Yok ki " + name)


def decode_seats(seats):
	#decode all the seat commands such as D2-15 D2 to D15 
	result = []
	for i in seats.split(" "):
		if("-" in i):
			rang = i.split("-")
			letter = i[0]
			start = rang[0][1:]
			last = rang[1]

			for i in range(int(start),int(last)+1):
				result.append(letter + str(i))

		else:
			result.append(i)


	return result


def isAvaliable(category_name,seats):
	seats = decode_seats(seats)
	layout = categories[category_name]["layout"]
	res = False
	for s in seats:
		row = lettoint(s[0])
		col = int(s[1:])
		if(layout[row][col] != "X"):
			break
	else:
		res = True
	return res


def checkifValid(category_name,seat):
	seats = decode_seats(seat)
	layout = categories[category_name]["layout"]
	for s in seats:
		row = lettoint(s[0])
		col = int(s[1:])

		if(row >= len(layout) and col < len(layout[0])):
			return ("Error: The category '{0}' has less row than the specified index " + seat + "!").format(category_name)
		if(col >= len(layout[0]) and row < len(layout)):
			return ("Error: The category '{0}' has less column than the specified index " + seat + "!").format(category_name)
		if(col >= len(layout[0]) and row >= len(layout)):
			return ("Error: The category '{0}' has less row and column than the specified index " + seat + "!").format(category_name)

	else:
		return "OK"






def sellticket(customer_name,c_type,category_name,seat):
	
	
	for s in seat.split(" "):
		s = s.strip()
		isvalid = checkifValid(category_name,s)
		if("-" in s):
			if(category_name in categories):
				if(isvalid != "OK"):
					print(isvalid)

				else:		

					if(isAvaliable(category_name,s) == False):
						print("Error: The seats " + s + " cannot be sold to {0} due some of them have already been sold!".format(customer_name))
					else:
						category = categories[category_name]
						for x in decode_seats(s):
							row = lettoint(x[0])
							col = int(x[1:])

							if(c_type == "student"):
								category["layout"][row][col] = "S"
								category["student"] = category["student"] + 1
								category["total"] = category["total"] + 10
							elif(c_type == "full"):
								category["layout"][row][col] = "F"
								category["full"] = category["full"] + 1
								category["total"] = category["total"] + 20
							elif(c_type == "season"):
								category["layout"][row][col] = "T"
								category["season"] = category["season"] + 1
								category["total"] = category["total"] + 250
							else:
								pass
						print("Success: {0} has bought {1} at {2}.".format(customer_name,s,category_name))

			else:
				print("yok")
		else:
			## s == A0,B1 etc.
			if(category_name in categories):
				category = categories[category_name]
				row = lettoint(s[0])
				col = int(s[1:])
				if(isvalid != "OK"):
					print(isvalid)

				else:
	
					if(category["layout"][row][col] != "X"):
						print("Warning: The seat " + s + " cannot be sold to {0} since it was already sold!".format(customer_name))
						
					else:
						##sell 
						if(c_type == "student"):
							category["layout"][row][col] = "S"
							category["student"] = category["student"] + 1
							category["total"] = category["total"] + 10
						elif(c_type == "full"):
							category["layout"][row][col] = "F"
							category["full"] = category["full"] + 1
							category["total"] = category["total"] + 20
						elif(c_type == "season"):
							category["layout"][row][col] = "T"
							category["season"] = category["season"] + 1
							category["total"] = category["total"] + 250
						else:
							pass

					print("Success: {0} has bought {1} at {2}.".format(customer_name,s,category_name))
			else:
				priny("Yok")



def cancel(category_name,seat):
	

	for s in seat.split(" "):
		s = s.strip()
		isvalid = checkifValid(category_name,s)
		if("-" in s):
			if(category_name in categories):
				if(isvalid != "OK"):
					print(isvalid)
				
				else:
					category = categories[category_name]
					for x in decode_seats(s):
						row = lettoint(x[0])
						col = int(x[1:])

						c_type = category["layout"][row][col]

						if(c_type == "X"):
							##already empty
							print("Warning: The seat {0} at '{1}' has already been free! Nothing to cancel".format(x,category_name))
						elif(c_type == "S"):
							##student
							category["student"] = category["student"] -1
							category["total"] = category["total"] - 10
							category["layout"][row][col] = "X"
							print("Success: The seat {0} at '{1}' has been cancelled and now ready to sell again".format(x,category_name))
						elif(c_type == "F"):
							category["full"] = category["full"] -1
							category["total"] = category["total"] - 20
							category["layout"][row][col] = "X"
							print("Success: The seat {0} at '{1}' has been cancelled and now ready to sell again".format(x,category_name))
						elif(c_type == "T"):
							category["season"] = category["season"] -1
							category["total"] = category["total"] - 250
							category["layout"][row][col] = "X"
							print("Success: The seat {0} at '{1}' has been cancelled and now ready to sell again".format(x,category_name))
						


			else:
				print("yok")
		else:
			## s == A0,B1 etc.
			if(category_name in categories):
				category = categories[category_name]
				row = lettoint(s[0])
				col = int(s[1:])
				if(isvalid != "OK"):
					print(isvalid)
					

				else:
					c_type = category["layout"][row][col]

					if(c_type == "X"):
						##already empty
						print("Error: The seat {0} at '{1}' has already been free! Nothing to cancel".format(s,category_name))
					elif(c_type == "S"):
						##student
						category["student"] = category["student"] -1
						category["total"] = category["total"] - 10
						category["layout"][row][col] = "X"
						print("Success: The seat {0} at '{1}' has been cancelled and now ready to sell again".format(s,category_name))
					elif(c_type == "F"):
						category["full"] = category["full"] -1
						category["total"] = category["total"] - 20
						category["layout"][row][col] = "X"
						print("Success: The seat {0} at '{1}' has been cancelled and now ready to sell again".format(s,category_name))
					elif(c_type == "T"):
						category["season"] = category["season"] -1
						category["total"] = category["total"] - 250
						category["layout"][row][col] = "X"
						print("Success: The seat {0} at '{1}' has been cancelled and now ready to sell again".format(s,category_name))
					

			else:
				priny("Yok")


def balance(category_name):
	if(category_name in categories):
		category = categories[category_name]
		print("Category report of '{0}'".format(category_name))
		print("-"*35)
		print("Sum of students = {0}, Sum of full pay = {1}, Sum of season ticket = {2}, and Revenues = {3} Dollars".format(category["student"],
			category["full"],category["season"],category["total"]))
	else:
		print("yok " + category_name)


if(len(sys.argv) > 1):
	inputfile = sys.argv[1]
	inFile = open(inputfile,"r")
	outFile = open("output.txt","w")
	commands = inFile.readlines()
	
	for com in  commands:
		comms = com.split(" ")
		if(comms[0] == "CREATECATEGORY"):
			if(len(comms) == 3):
				create_category(comms[1],int(comms[2].split("x")[0]),int(comms[2].split("x")[1].strip()))
			else:
				print("Unrecognized command size")

		elif(comms[0] == "SELLTICKET"):
			if(len(comms) > 4):
				seat = ""
				for i in range(4,len(comms)-1):
					seat += comms[i] + " "
				seat+= comms[-1]

				sellticket(comms[1],comms[2],comms[3],seat)
			else:
				print("Unrecognized command size")
		elif(comms[0] == "CANCELTICKET"):
			if(len(comms) > 2):
				seat = ""
				for i in range(2,len(comms)-1):
					seat += comms[i] + " "
				seat+= comms[-1]
				cancel(comms[1],seat)
			else:
				print("Unrecognized command size")
		elif(comms[0] == "SHOWCATEGORY"):
			if(len(comms) == 2):
				print_category(comms[1].strip())
			else:
				print("Unrecognized command size")
		elif(comms[0] == "BALANCE"):
			if(len(comms) == 2):
				balance(comms[1].strip())
			else:
				print("Unrecognized command size")
		else:
			print("Unrecognized Command")

	categories.clear()
	sys.stdout = outFile

	for com in  commands:
		comms = com.split(" ")
		if(comms[0] == "CREATECATEGORY"):
			if(len(comms) == 3):
				create_category(comms[1],int(comms[2].split("x")[0]),int(comms[2].split("x")[1].strip()))
			else:
				print("Unrecognized command size")

		elif(comms[0] == "SELLTICKET"):
			if(len(comms) > 4):
				seat = ""
				for i in range(4,len(comms)-1):
					seat += comms[i] + " "
				seat+= comms[-1]

				sellticket(comms[1],comms[2],comms[3],seat)
			else:
				print("Unrecognized command size")
		elif(comms[0] == "CANCELTICKET"):
			if(len(comms) > 2):
				seat = ""
				for i in range(2,len(comms)-1):
					seat += comms[i] + " "
				seat+= comms[-1]
				cancel(comms[1],seat)
			else:
				print("Unrecognized command size")
		elif(comms[0] == "SHOWCATEGORY"):
			if(len(comms) == 2):
				print_category(comms[1].strip())
			else:
				print("Unrecognized command size")
		elif(comms[0] == "BALANCE"):
			if(len(comms) == 2):
				balance(comms[1].strip())
			else:
				print("Unrecognized command size")
		else:
			print("Unrecognized Command")


	outFile.close()
	inFile.close()
