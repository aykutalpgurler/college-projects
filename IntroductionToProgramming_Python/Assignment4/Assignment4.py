import sys

def lettoint(letter):
	#Function that takes in letter and returns its corresponding integer value.
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
	#Function that takes in integer and returns its corresponding letter value.
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

def readTXT(txt):
	#Function to read text files
    with open(txt, "r", encoding="utf8") as f:
        lines = f.readlines()
        input_list=[]
        for line in lines:
            input_list.append(line.strip()) #Remove leading and trailing white space from each line
    return input_list

def shipsPOSITIONS(txt):
	#Function which creates dictionary of positions for each player.
    positionsDict={}
    row_num=0
    for row in txt:
        column_num=0
        row_num+=1
        row=row.split(";")
        for i in row:
            o_xDict={}
            if i == "":
                o_xDict["N"]="-"
            else:
                o_xDict[i]="-"
            positionsDict[f"{row_num}{inttolet(column_num)}"]=o_xDict
            column_num+=1
	#Example of Player1's positionDict's some part: {.....,'2D':{'N':'-'},'2E':{'B':'-'},.....}
    return positionsDict

def listPOSITIONS(positionsDict):
	#Function which returns dictionary with values of lists of positions for Carrier, Destroyer and Submarine ships.
	shipDict={"Carrier":[],"Battleship1":[],"Battleship2":[],"Destroyer":[],"Submarine":[],"PatrolBoat1":[],"PatrolBoat2":[],"PatrolBoat3":[],"PatrolBoat4":[]}
	for anahtar in positionsDict:
		for letter in positionsDict[anahtar].keys():
			if letter == "C":
				shipDict["Carrier"]+=[anahtar]
			elif letter == "D":
				shipDict["Destroyer"]+=[anahtar]
			elif letter == "S":
				shipDict["Submarine"]+=[anahtar]
	return shipDict

def finalshipdict(txt,positionsDict):
	#Function which uses "OptionalPlayer.txt" files and returns dictionary with values of final lists of positions for each type of ships.
	shipDict=listPOSITIONS(positionsDict)
	for line in txt:
		positionlistesi=[]
		shiptype=line.split(":")[0]
		startrow=int(line.split(":")[1].split(";")[0].split(",")[0])
		startcol=lettoint(line.split(":")[1].split(";")[0].split(",")[1])
		rightordown=line.split(":")[1].split(";")[1]
		if shiptype == "B1":
			if rightordown =="right":
				positionlistesi=[f"{startrow}{inttolet(startcol)}",f"{startrow}{inttolet(startcol+1)}",f"{startrow}{inttolet(startcol+2)}",f"{startrow}{inttolet(startcol+3)}"]
				shipDict["Battleship1"]=positionlistesi
			if rightordown =="down":
				positionlistesi=[f"{startrow}{inttolet(startcol)}",f"{startrow+1}{inttolet(startcol)}",f"{startrow+2}{inttolet(startcol)}",f"{startrow+3}{inttolet(startcol)}"]
				shipDict["Battleship1"]=positionlistesi
		elif shiptype == "B2":
			if rightordown =="right":
				positionlistesi=[f"{startrow}{inttolet(startcol)}",f"{startrow}{inttolet(startcol+1)}",f"{startrow}{inttolet(startcol+2)}",f"{startrow}{inttolet(startcol+3)}"]
				shipDict["Battleship2"]=positionlistesi
			if rightordown =="down":
				positionlistesi=[f"{startrow}{inttolet(startcol)}",f"{startrow+1}{inttolet(startcol)}",f"{startrow+2}{inttolet(startcol)}",f"{startrow+3}{inttolet(startcol)}"]
				shipDict["Battleship2"]=positionlistesi
		elif shiptype == "P1":
			if rightordown =="right":
				positionlistesi=[f"{startrow}{inttolet(startcol)}",f"{startrow}{inttolet(startcol+1)}"]
				shipDict["PatrolBoat1"]=positionlistesi
			if rightordown =="down":
				positionlistesi=[f"{startrow}{inttolet(startcol)}",f"{startrow+1}{inttolet(startcol)}"]
				shipDict["PatrolBoat1"]=positionlistesi
		elif shiptype == "P2":
			if rightordown =="right":
				positionlistesi=[f"{startrow}{inttolet(startcol)}",f"{startrow}{inttolet(startcol+1)}"]
				shipDict["PatrolBoat2"]=positionlistesi
			if rightordown =="down":
				positionlistesi=[f"{startrow}{inttolet(startcol)}",f"{startrow+1}{inttolet(startcol)}"]
				shipDict["PatrolBoat2"]=positionlistesi
		elif shiptype == "P3":
			if rightordown =="right":
				positionlistesi=[f"{startrow}{inttolet(startcol)}",f"{startrow}{inttolet(startcol+1)}"]
				shipDict["PatrolBoat3"]=positionlistesi
			if rightordown =="down":
				positionlistesi=[f"{startrow}{inttolet(startcol)}",f"{startrow+1}{inttolet(startcol)}"]
				shipDict["PatrolBoat3"]=positionlistesi
		else:
			if rightordown =="right":
				positionlistesi=[f"{startrow}{inttolet(startcol)}",f"{startrow}{inttolet(startcol+1)}"]
				shipDict["PatrolBoat4"]=positionlistesi
			if rightordown =="down":
				positionlistesi=[f"{startrow}{inttolet(startcol)}",f"{startrow+1}{inttolet(startcol)}"]
				shipDict["PatrolBoat4"]=positionlistesi
	#finalshipdict returns shipDict for Player1 : {'Carrier': ['1G', '2G', '3G', '4G', '5G'], 'Battleship1': ['6B', '6C', '6D', '6E'], 'Battleship2': ['2E', '3E', '4E', '5E'], 'Destroyer': ['8J', '9J', '10J'], 'Submarine': ['7F', '7G', '7H'], 'PatrolBoat1': ['3B', '4B'], 'PatrolBoat2': ['10B', '10C'], 'PatrolBoat3': ['9E', '9F'], 'PatrolBoat4': ['3H', '3I']}
	return shipDict

def playerXmoves(intxt):
	#Function which uses "Player.in" files and returns list of moves for each players.
	strfromlist=""
	for line in intxt:
		strfromlist=line
	listfromstr=strfromlist.split(";")[:-1]
	return listfromstr

def VALFUNC(dictnkey):
	#Function which takes dictionary[key] as input and returns nested dictionary's value.
	#This function was essential to use my positionDict efficiently.
	'''
	Example use
	VALFUNC(positionDict1['2E']) returns: '-'
	'''
	for i in dictnkey.keys():
		return dictnkey[i]

def KEYFUNC(dictnkey):
	#Function which takes dictionary[key] as input and returns nested dictionary's key.
	#This function was essential to use my positionDict efficiently.
	'''
	Example use
	KEYFUNC(positionDict1['2E']) returns: 'B'
	'''
	for i in dictnkey.keys():
		return i
	
def processingMoves(positionsDict,shipDict,move,Cinfo,Binfo,Dinfo,Sinfo,Pinfo):
	#Function which takes 8 input and process moves in positionDict.
	if KEYFUNC(positionsDict[move])=="N":
		positionsDict[move][KEYFUNC(positionsDict[move])]="O"
	if KEYFUNC(positionsDict[move])=="C":
		positionsDict[move][KEYFUNC(positionsDict[move])]="X"
		c=0
		for position in shipDict["Carrier"]:
			if positionsDict[position][KEYFUNC(positionsDict[position])]=="X":
				c+=1
				if c == 5:
					Cinfo[0]="Carrier\t\tX"
	if KEYFUNC(positionsDict[move])=="B":
		positionsDict[move][KEYFUNC(positionsDict[move])]="X"
		if move in shipDict["Battleship1"]:
			b1=0
			for position in shipDict["Battleship1"]:
				if positionsDict[position][KEYFUNC(positionsDict[position])]=="X":
					b1+=1
					if b1 == 4:
						if Binfo[0][-3]=="-":
							Binfo[0]="Battleship\tX -"
						else:
							Binfo[0]="Battleship\tX X"
				else:
					break
		if move in shipDict["Battleship2"]:
			b2=0
			for position in shipDict["Battleship2"]:
				if positionsDict[position][KEYFUNC(positionsDict[position])]=="X":
					b2+=1
					if b2 == 4:
						if Binfo[0][-3]=="-":
							Binfo[0]="Battleship\tX -"
						else:
							Binfo[0]="Battleship\tX X"
				else:
					break
	if KEYFUNC(positionsDict[move])=="D":
		positionsDict[move][KEYFUNC(positionsDict[move])]="X"
		d=0
		for position in shipDict["Destroyer"]:
			if positionsDict[position][KEYFUNC(positionsDict[position])]=="X":
				d+=1
				if d == 3:
					Dinfo[0]="Destroyer\tX"
	if KEYFUNC(positionsDict[move])=="S":
		positionsDict[move][KEYFUNC(positionsDict[move])]="X"
		s=0
		for position in shipDict["Submarine"]:
			if positionsDict[position][KEYFUNC(positionsDict[position])]=="X":
				s+=1
				if s == 3:
					Sinfo[0]="Submarine\tX"
	if KEYFUNC(positionsDict[move])=="P":
		positionsDict[move][KEYFUNC(positionsDict[move])]="X"
		if move in shipDict["PatrolBoat1"]:
			p1=0
			for position in shipDict["PatrolBoat1"]:
				if positionsDict[position][KEYFUNC(positionsDict[position])]=="X":
					p1+=1
					if p1 == 2:
						if Pinfo[0][-7]=="-":
							Pinfo[0]="Patrol Boat\tX - - -"
						elif Pinfo[0][-5]=="-":
							Pinfo[0]="Patrol Boat\tX X - -"
						elif Pinfo[0][-3]=="-":
							Pinfo[0]="Patrol Boat\tX X X -"
						else:
							Pinfo[0]="Patrol Boat\tX X X X"
				else:
					break
		if move in shipDict["PatrolBoat2"]:
			p2=0
			for position in shipDict["PatrolBoat2"]:
				if positionsDict[position][KEYFUNC(positionsDict[position])]=="X":
					p2+=1
					if p2 == 2:
						if Pinfo[0][-7]=="-":
							Pinfo[0]="Patrol Boat\tX - - -"
						elif Pinfo[0][-5]=="-":
							Pinfo[0]="Patrol Boat\tX X - -"
						elif Pinfo[0][-3]=="-":
							Pinfo[0]="Patrol Boat\tX X X -"
						else:
							Pinfo[0]="Patrol Boat\tX X X X"
				else:
					break
		if move in shipDict["PatrolBoat3"]:
			p3=0
			for position in shipDict["PatrolBoat3"]:
				if positionsDict[position][KEYFUNC(positionsDict[position])]=="X":
					p3+=1
					if p3 == 2:
						if Pinfo[0][-7]=="-":
							Pinfo[0]="Patrol Boat\tX - - -"
						elif Pinfo[0][-5]=="-":
							Pinfo[0]="Patrol Boat\tX X - -"
						elif Pinfo[0][-3]=="-":
							Pinfo[0]="Patrol Boat\tX X X -"
						else:
							Pinfo[0]="Patrol Boat\tX X X X"
				else:
					break
		if move in shipDict["PatrolBoat4"]:
			p4=0
			for position in shipDict["PatrolBoat4"]:
				if positionsDict[position][KEYFUNC(positionsDict[position])]=="X":
					p4+=1
					if p4 == 2:
						if Pinfo[0][-7]=="-":
							Pinfo[0]="Patrol Boat\tX - - -"
						elif Pinfo[0][-5]=="-":
							Pinfo[0]="Patrol Boat\tX X - -"
						elif Pinfo[0][-3]=="-":
							Pinfo[0]="Patrol Boat\tX X X -"
						else:
							Pinfo[0]="Patrol Boat\tX X X X"
				else:
					break

def finalInfo(dictnkey):
	#Function which shows unhitten ships in final information.
	if VALFUNC(dictnkey)=="-":
		if KEYFUNC(dictnkey) != "N":
			return KEYFUNC(dictnkey) #If a ship's position is not hit function returns its key. Else function returns value "O" or "X".
		else:
			return VALFUNC(dictnkey)
	else:
		return VALFUNC(dictnkey)

def printFINALINFO():
	#Function to print final information.
	print()
	print("Final Information")
	print()
	print(f"Player1's Board\t\t\t\tPlayer2's Board")
	print(f"  A B C D E F G H I J\t\t  A B C D E F G H I J")
	print(f"1 {finalInfo(positionsDict1['1A'])} {finalInfo(positionsDict1['1B'])} {finalInfo(positionsDict1['1C'])} {finalInfo(positionsDict1['1D'])} {finalInfo(positionsDict1['1E'])} {finalInfo(positionsDict1['1F'])} {finalInfo(positionsDict1['1G'])} {finalInfo(positionsDict1['1H'])} {finalInfo(positionsDict1['1I'])} {finalInfo(positionsDict1['1J'])}\t\t1 {finalInfo(positionsDict2['1A'])} {finalInfo(positionsDict2['1B'])} {finalInfo(positionsDict2['1C'])} {finalInfo(positionsDict2['1D'])} {finalInfo(positionsDict2['1E'])} {finalInfo(positionsDict2['1F'])} {finalInfo(positionsDict2['1G'])} {finalInfo(positionsDict2['1H'])} {finalInfo(positionsDict2['1I'])} {finalInfo(positionsDict2['1J'])}")
	print(f"2 {finalInfo(positionsDict1['2A'])} {finalInfo(positionsDict1['2B'])} {finalInfo(positionsDict1['2C'])} {finalInfo(positionsDict1['2D'])} {finalInfo(positionsDict1['2E'])} {finalInfo(positionsDict1['2F'])} {finalInfo(positionsDict1['2G'])} {finalInfo(positionsDict1['2H'])} {finalInfo(positionsDict1['2I'])} {finalInfo(positionsDict1['2J'])}\t\t2 {finalInfo(positionsDict2['2A'])} {finalInfo(positionsDict2['2B'])} {finalInfo(positionsDict2['2C'])} {finalInfo(positionsDict2['2D'])} {finalInfo(positionsDict2['2E'])} {finalInfo(positionsDict2['2F'])} {finalInfo(positionsDict2['2G'])} {finalInfo(positionsDict2['2H'])} {finalInfo(positionsDict2['2I'])} {finalInfo(positionsDict2['2J'])}")
	print(f"3 {finalInfo(positionsDict1['3A'])} {finalInfo(positionsDict1['3B'])} {finalInfo(positionsDict1['3C'])} {finalInfo(positionsDict1['3D'])} {finalInfo(positionsDict1['3E'])} {finalInfo(positionsDict1['3F'])} {finalInfo(positionsDict1['3G'])} {finalInfo(positionsDict1['3H'])} {finalInfo(positionsDict1['3I'])} {finalInfo(positionsDict1['3J'])}\t\t3 {finalInfo(positionsDict2['3A'])} {finalInfo(positionsDict2['3B'])} {finalInfo(positionsDict2['3C'])} {finalInfo(positionsDict2['3D'])} {finalInfo(positionsDict2['3E'])} {finalInfo(positionsDict2['3F'])} {finalInfo(positionsDict2['3G'])} {finalInfo(positionsDict2['3H'])} {finalInfo(positionsDict2['3I'])} {finalInfo(positionsDict2['3J'])}")
	print(f"4 {finalInfo(positionsDict1['4A'])} {finalInfo(positionsDict1['4B'])} {finalInfo(positionsDict1['4C'])} {finalInfo(positionsDict1['4D'])} {finalInfo(positionsDict1['4E'])} {finalInfo(positionsDict1['4F'])} {finalInfo(positionsDict1['4G'])} {finalInfo(positionsDict1['4H'])} {finalInfo(positionsDict1['4I'])} {finalInfo(positionsDict1['4J'])}\t\t4 {finalInfo(positionsDict2['4A'])} {finalInfo(positionsDict2['4B'])} {finalInfo(positionsDict2['4C'])} {finalInfo(positionsDict2['4D'])} {finalInfo(positionsDict2['4E'])} {finalInfo(positionsDict2['4F'])} {finalInfo(positionsDict2['4G'])} {finalInfo(positionsDict2['4H'])} {finalInfo(positionsDict2['4I'])} {finalInfo(positionsDict2['4J'])}")
	print(f"5 {finalInfo(positionsDict1['5A'])} {finalInfo(positionsDict1['5B'])} {finalInfo(positionsDict1['5C'])} {finalInfo(positionsDict1['5D'])} {finalInfo(positionsDict1['5E'])} {finalInfo(positionsDict1['5F'])} {finalInfo(positionsDict1['5G'])} {finalInfo(positionsDict1['5H'])} {finalInfo(positionsDict1['5I'])} {finalInfo(positionsDict1['5J'])}\t\t5 {finalInfo(positionsDict2['5A'])} {finalInfo(positionsDict2['5B'])} {finalInfo(positionsDict2['5C'])} {finalInfo(positionsDict2['5D'])} {finalInfo(positionsDict2['5E'])} {finalInfo(positionsDict2['5F'])} {finalInfo(positionsDict2['5G'])} {finalInfo(positionsDict2['5H'])} {finalInfo(positionsDict2['5I'])} {finalInfo(positionsDict2['5J'])}")
	print(f"6 {finalInfo(positionsDict1['6A'])} {finalInfo(positionsDict1['6B'])} {finalInfo(positionsDict1['6C'])} {finalInfo(positionsDict1['6D'])} {finalInfo(positionsDict1['6E'])} {finalInfo(positionsDict1['6F'])} {finalInfo(positionsDict1['6G'])} {finalInfo(positionsDict1['6H'])} {finalInfo(positionsDict1['6I'])} {finalInfo(positionsDict1['6J'])}\t\t6 {finalInfo(positionsDict2['6A'])} {finalInfo(positionsDict2['6B'])} {finalInfo(positionsDict2['6C'])} {finalInfo(positionsDict2['6D'])} {finalInfo(positionsDict2['6E'])} {finalInfo(positionsDict2['6F'])} {finalInfo(positionsDict2['6G'])} {finalInfo(positionsDict2['6H'])} {finalInfo(positionsDict2['6I'])} {finalInfo(positionsDict2['6J'])}")
	print(f"7 {finalInfo(positionsDict1['7A'])} {finalInfo(positionsDict1['7B'])} {finalInfo(positionsDict1['7C'])} {finalInfo(positionsDict1['7D'])} {finalInfo(positionsDict1['7E'])} {finalInfo(positionsDict1['7F'])} {finalInfo(positionsDict1['7G'])} {finalInfo(positionsDict1['7H'])} {finalInfo(positionsDict1['7I'])} {finalInfo(positionsDict1['7J'])}\t\t7 {finalInfo(positionsDict2['7A'])} {finalInfo(positionsDict2['7B'])} {finalInfo(positionsDict2['7C'])} {finalInfo(positionsDict2['7D'])} {finalInfo(positionsDict2['7E'])} {finalInfo(positionsDict2['7F'])} {finalInfo(positionsDict2['7G'])} {finalInfo(positionsDict2['7H'])} {finalInfo(positionsDict2['7I'])} {finalInfo(positionsDict2['7J'])}")
	print(f"8 {finalInfo(positionsDict1['8A'])} {finalInfo(positionsDict1['8B'])} {finalInfo(positionsDict1['8C'])} {finalInfo(positionsDict1['8D'])} {finalInfo(positionsDict1['8E'])} {finalInfo(positionsDict1['8F'])} {finalInfo(positionsDict1['8G'])} {finalInfo(positionsDict1['8H'])} {finalInfo(positionsDict1['8I'])} {finalInfo(positionsDict1['8J'])}\t\t8 {finalInfo(positionsDict2['8A'])} {finalInfo(positionsDict2['8B'])} {finalInfo(positionsDict2['8C'])} {finalInfo(positionsDict2['8D'])} {finalInfo(positionsDict2['8E'])} {finalInfo(positionsDict2['8F'])} {finalInfo(positionsDict2['8G'])} {finalInfo(positionsDict2['8H'])} {finalInfo(positionsDict2['8I'])} {finalInfo(positionsDict2['8J'])}")
	print(f"9 {finalInfo(positionsDict1['9A'])} {finalInfo(positionsDict1['9B'])} {finalInfo(positionsDict1['9C'])} {finalInfo(positionsDict1['9D'])} {finalInfo(positionsDict1['9E'])} {finalInfo(positionsDict1['9F'])} {finalInfo(positionsDict1['9G'])} {finalInfo(positionsDict1['9H'])} {finalInfo(positionsDict1['9I'])} {finalInfo(positionsDict1['9J'])}\t\t9 {finalInfo(positionsDict2['9A'])} {finalInfo(positionsDict2['9B'])} {finalInfo(positionsDict2['9C'])} {finalInfo(positionsDict2['9D'])} {finalInfo(positionsDict2['9E'])} {finalInfo(positionsDict2['9F'])} {finalInfo(positionsDict2['9G'])} {finalInfo(positionsDict2['9H'])} {finalInfo(positionsDict2['9I'])} {finalInfo(positionsDict2['9J'])}")
	print(f"10{finalInfo(positionsDict1['10A'])} {finalInfo(positionsDict1['10B'])} {finalInfo(positionsDict1['10C'])} {finalInfo(positionsDict1['10D'])} {finalInfo(positionsDict1['10E'])} {finalInfo(positionsDict1['10F'])} {finalInfo(positionsDict1['10G'])} {finalInfo(positionsDict1['10H'])} {finalInfo(positionsDict1['10I'])} {finalInfo(positionsDict1['10J'])}\t\t10{finalInfo(positionsDict2['10A'])} {finalInfo(positionsDict2['10B'])} {finalInfo(positionsDict2['10C'])} {finalInfo(positionsDict2['10D'])} {finalInfo(positionsDict2['10E'])} {finalInfo(positionsDict2['10F'])} {finalInfo(positionsDict2['10G'])} {finalInfo(positionsDict2['10H'])} {finalInfo(positionsDict2['10I'])} {finalInfo(positionsDict2['10J'])}")
	print()
	print(f"{Cinfo1[0]}\t\t\t\t{Cinfo2[0]}")
	print(f"{Binfo1[0]}\t\t\t\t{Binfo2[0]}")
	print(f"{Dinfo1[0]}\t\t\t\t{Dinfo2[0]}")
	print(f"{Sinfo1[0]}\t\t\t\t{Sinfo2[0]}")
	print(f"{Pinfo1[0]}\t\t\t{Pinfo2[0]}")

def writeFINALINFO():
	#Function to write final information to text file.
	f.write("Final Information\n\n")
	f.write(f"Player1's Board\t\t\t\tPlayer2's Board\n")
	f.write(f"  A B C D E F G H I J\t\t  A B C D E F G H I J\n")
	f.write(f"1 {finalInfo(positionsDict1['1A'])} {finalInfo(positionsDict1['1B'])} {finalInfo(positionsDict1['1C'])} {finalInfo(positionsDict1['1D'])} {finalInfo(positionsDict1['1E'])} {finalInfo(positionsDict1['1F'])} {finalInfo(positionsDict1['1G'])} {finalInfo(positionsDict1['1H'])} {finalInfo(positionsDict1['1I'])} {finalInfo(positionsDict1['1J'])}\t\t1 {finalInfo(positionsDict2['1A'])} {finalInfo(positionsDict2['1B'])} {finalInfo(positionsDict2['1C'])} {finalInfo(positionsDict2['1D'])} {finalInfo(positionsDict2['1E'])} {finalInfo(positionsDict2['1F'])} {finalInfo(positionsDict2['1G'])} {finalInfo(positionsDict2['1H'])} {finalInfo(positionsDict2['1I'])} {finalInfo(positionsDict2['1J'])}\n")
	f.write(f"2 {finalInfo(positionsDict1['2A'])} {finalInfo(positionsDict1['2B'])} {finalInfo(positionsDict1['2C'])} {finalInfo(positionsDict1['2D'])} {finalInfo(positionsDict1['2E'])} {finalInfo(positionsDict1['2F'])} {finalInfo(positionsDict1['2G'])} {finalInfo(positionsDict1['2H'])} {finalInfo(positionsDict1['2I'])} {finalInfo(positionsDict1['2J'])}\t\t2 {finalInfo(positionsDict2['2A'])} {finalInfo(positionsDict2['2B'])} {finalInfo(positionsDict2['2C'])} {finalInfo(positionsDict2['2D'])} {finalInfo(positionsDict2['2E'])} {finalInfo(positionsDict2['2F'])} {finalInfo(positionsDict2['2G'])} {finalInfo(positionsDict2['2H'])} {finalInfo(positionsDict2['2I'])} {finalInfo(positionsDict2['2J'])}\n")
	f.write(f"3 {finalInfo(positionsDict1['3A'])} {finalInfo(positionsDict1['3B'])} {finalInfo(positionsDict1['3C'])} {finalInfo(positionsDict1['3D'])} {finalInfo(positionsDict1['3E'])} {finalInfo(positionsDict1['3F'])} {finalInfo(positionsDict1['3G'])} {finalInfo(positionsDict1['3H'])} {finalInfo(positionsDict1['3I'])} {finalInfo(positionsDict1['3J'])}\t\t3 {finalInfo(positionsDict2['3A'])} {finalInfo(positionsDict2['3B'])} {finalInfo(positionsDict2['3C'])} {finalInfo(positionsDict2['3D'])} {finalInfo(positionsDict2['3E'])} {finalInfo(positionsDict2['3F'])} {finalInfo(positionsDict2['3G'])} {finalInfo(positionsDict2['3H'])} {finalInfo(positionsDict2['3I'])} {finalInfo(positionsDict2['3J'])}\n")
	f.write(f"4 {finalInfo(positionsDict1['4A'])} {finalInfo(positionsDict1['4B'])} {finalInfo(positionsDict1['4C'])} {finalInfo(positionsDict1['4D'])} {finalInfo(positionsDict1['4E'])} {finalInfo(positionsDict1['4F'])} {finalInfo(positionsDict1['4G'])} {finalInfo(positionsDict1['4H'])} {finalInfo(positionsDict1['4I'])} {finalInfo(positionsDict1['4J'])}\t\t4 {finalInfo(positionsDict2['4A'])} {finalInfo(positionsDict2['4B'])} {finalInfo(positionsDict2['4C'])} {finalInfo(positionsDict2['4D'])} {finalInfo(positionsDict2['4E'])} {finalInfo(positionsDict2['4F'])} {finalInfo(positionsDict2['4G'])} {finalInfo(positionsDict2['4H'])} {finalInfo(positionsDict2['4I'])} {finalInfo(positionsDict2['4J'])}\n")
	f.write(f"5 {finalInfo(positionsDict1['5A'])} {finalInfo(positionsDict1['5B'])} {finalInfo(positionsDict1['5C'])} {finalInfo(positionsDict1['5D'])} {finalInfo(positionsDict1['5E'])} {finalInfo(positionsDict1['5F'])} {finalInfo(positionsDict1['5G'])} {finalInfo(positionsDict1['5H'])} {finalInfo(positionsDict1['5I'])} {finalInfo(positionsDict1['5J'])}\t\t5 {finalInfo(positionsDict2['5A'])} {finalInfo(positionsDict2['5B'])} {finalInfo(positionsDict2['5C'])} {finalInfo(positionsDict2['5D'])} {finalInfo(positionsDict2['5E'])} {finalInfo(positionsDict2['5F'])} {finalInfo(positionsDict2['5G'])} {finalInfo(positionsDict2['5H'])} {finalInfo(positionsDict2['5I'])} {finalInfo(positionsDict2['5J'])}\n")
	f.write(f"6 {finalInfo(positionsDict1['6A'])} {finalInfo(positionsDict1['6B'])} {finalInfo(positionsDict1['6C'])} {finalInfo(positionsDict1['6D'])} {finalInfo(positionsDict1['6E'])} {finalInfo(positionsDict1['6F'])} {finalInfo(positionsDict1['6G'])} {finalInfo(positionsDict1['6H'])} {finalInfo(positionsDict1['6I'])} {finalInfo(positionsDict1['6J'])}\t\t6 {finalInfo(positionsDict2['6A'])} {finalInfo(positionsDict2['6B'])} {finalInfo(positionsDict2['6C'])} {finalInfo(positionsDict2['6D'])} {finalInfo(positionsDict2['6E'])} {finalInfo(positionsDict2['6F'])} {finalInfo(positionsDict2['6G'])} {finalInfo(positionsDict2['6H'])} {finalInfo(positionsDict2['6I'])} {finalInfo(positionsDict2['6J'])}\n")
	f.write(f"7 {finalInfo(positionsDict1['7A'])} {finalInfo(positionsDict1['7B'])} {finalInfo(positionsDict1['7C'])} {finalInfo(positionsDict1['7D'])} {finalInfo(positionsDict1['7E'])} {finalInfo(positionsDict1['7F'])} {finalInfo(positionsDict1['7G'])} {finalInfo(positionsDict1['7H'])} {finalInfo(positionsDict1['7I'])} {finalInfo(positionsDict1['7J'])}\t\t7 {finalInfo(positionsDict2['7A'])} {finalInfo(positionsDict2['7B'])} {finalInfo(positionsDict2['7C'])} {finalInfo(positionsDict2['7D'])} {finalInfo(positionsDict2['7E'])} {finalInfo(positionsDict2['7F'])} {finalInfo(positionsDict2['7G'])} {finalInfo(positionsDict2['7H'])} {finalInfo(positionsDict2['7I'])} {finalInfo(positionsDict2['7J'])}\n")
	f.write(f"8 {finalInfo(positionsDict1['8A'])} {finalInfo(positionsDict1['8B'])} {finalInfo(positionsDict1['8C'])} {finalInfo(positionsDict1['8D'])} {finalInfo(positionsDict1['8E'])} {finalInfo(positionsDict1['8F'])} {finalInfo(positionsDict1['8G'])} {finalInfo(positionsDict1['8H'])} {finalInfo(positionsDict1['8I'])} {finalInfo(positionsDict1['8J'])}\t\t8 {finalInfo(positionsDict2['8A'])} {finalInfo(positionsDict2['8B'])} {finalInfo(positionsDict2['8C'])} {finalInfo(positionsDict2['8D'])} {finalInfo(positionsDict2['8E'])} {finalInfo(positionsDict2['8F'])} {finalInfo(positionsDict2['8G'])} {finalInfo(positionsDict2['8H'])} {finalInfo(positionsDict2['8I'])} {finalInfo(positionsDict2['8J'])}\n")
	f.write(f"9 {finalInfo(positionsDict1['9A'])} {finalInfo(positionsDict1['9B'])} {finalInfo(positionsDict1['9C'])} {finalInfo(positionsDict1['9D'])} {finalInfo(positionsDict1['9E'])} {finalInfo(positionsDict1['9F'])} {finalInfo(positionsDict1['9G'])} {finalInfo(positionsDict1['9H'])} {finalInfo(positionsDict1['9I'])} {finalInfo(positionsDict1['9J'])}\t\t9 {finalInfo(positionsDict2['9A'])} {finalInfo(positionsDict2['9B'])} {finalInfo(positionsDict2['9C'])} {finalInfo(positionsDict2['9D'])} {finalInfo(positionsDict2['9E'])} {finalInfo(positionsDict2['9F'])} {finalInfo(positionsDict2['9G'])} {finalInfo(positionsDict2['9H'])} {finalInfo(positionsDict2['9I'])} {finalInfo(positionsDict2['9J'])}\n")
	f.write(f"10{finalInfo(positionsDict1['10A'])} {finalInfo(positionsDict1['10B'])} {finalInfo(positionsDict1['10C'])} {finalInfo(positionsDict1['10D'])} {finalInfo(positionsDict1['10E'])} {finalInfo(positionsDict1['10F'])} {finalInfo(positionsDict1['10G'])} {finalInfo(positionsDict1['10H'])} {finalInfo(positionsDict1['10I'])} {finalInfo(positionsDict1['10J'])}\t\t10{finalInfo(positionsDict2['10A'])} {finalInfo(positionsDict2['10B'])} {finalInfo(positionsDict2['10C'])} {finalInfo(positionsDict2['10D'])} {finalInfo(positionsDict2['10E'])} {finalInfo(positionsDict2['10F'])} {finalInfo(positionsDict2['10G'])} {finalInfo(positionsDict2['10H'])} {finalInfo(positionsDict2['10I'])} {finalInfo(positionsDict2['10J'])}\n\n")
	f.write(f"{Cinfo1[0]}\t\t\t\t{Cinfo2[0]}\n")
	f.write(f"{Binfo1[0]}\t\t\t\t{Binfo2[0]}\n")
	f.write(f"{Dinfo1[0]}\t\t\t\t{Dinfo2[0]}\n")
	f.write(f"{Sinfo1[0]}\t\t\t\t{Sinfo2[0]}\n")
	f.write(f"{Pinfo1[0]}\t\t\t{Pinfo2[0]}\n")

def PRINT_IT(move,turn):
	#Function which prints and writes output during game.
	if turn==True:
		global roundnum # I used 'global' keyword due to I had UnboundLocalError.
		roundnum += 1
		print("Player1's Move")
		print()
		f.write(f"Player1's Move\n\n")
	else:
		print("Player2's Move")
		print()
		f.write(f"Player2's Move\n\n")
	print(f"Round : {roundnum}\t\t\t\t\tGrid Size: 10x10")
	print()
	print(f"Player1's Hidden Board\t\tPlayer2's Hidden Board")
	print(f"  A B C D E F G H I J\t\t  A B C D E F G H I J")
	print(f"1 {VALFUNC(positionsDict1['1A'])} {VALFUNC(positionsDict1['1B'])} {VALFUNC(positionsDict1['1C'])} {VALFUNC(positionsDict1['1D'])} {VALFUNC(positionsDict1['1E'])} {VALFUNC(positionsDict1['1F'])} {VALFUNC(positionsDict1['1G'])} {VALFUNC(positionsDict1['1H'])} {VALFUNC(positionsDict1['1I'])} {VALFUNC(positionsDict1['1J'])}\t\t1 {VALFUNC(positionsDict2['1A'])} {VALFUNC(positionsDict2['1B'])} {VALFUNC(positionsDict2['1C'])} {VALFUNC(positionsDict2['1D'])} {VALFUNC(positionsDict2['1E'])} {VALFUNC(positionsDict2['1F'])} {VALFUNC(positionsDict2['1G'])} {VALFUNC(positionsDict2['1H'])} {VALFUNC(positionsDict2['1I'])} {VALFUNC(positionsDict2['1J'])}")
	print(f"2 {VALFUNC(positionsDict1['2A'])} {VALFUNC(positionsDict1['2B'])} {VALFUNC(positionsDict1['2C'])} {VALFUNC(positionsDict1['2D'])} {VALFUNC(positionsDict1['2E'])} {VALFUNC(positionsDict1['2F'])} {VALFUNC(positionsDict1['2G'])} {VALFUNC(positionsDict1['2H'])} {VALFUNC(positionsDict1['2I'])} {VALFUNC(positionsDict1['2J'])}\t\t2 {VALFUNC(positionsDict2['2A'])} {VALFUNC(positionsDict2['2B'])} {VALFUNC(positionsDict2['2C'])} {VALFUNC(positionsDict2['2D'])} {VALFUNC(positionsDict2['2E'])} {VALFUNC(positionsDict2['2F'])} {VALFUNC(positionsDict2['2G'])} {VALFUNC(positionsDict2['2H'])} {VALFUNC(positionsDict2['2I'])} {VALFUNC(positionsDict2['2J'])}")
	print(f"3 {VALFUNC(positionsDict1['3A'])} {VALFUNC(positionsDict1['3B'])} {VALFUNC(positionsDict1['3C'])} {VALFUNC(positionsDict1['3D'])} {VALFUNC(positionsDict1['3E'])} {VALFUNC(positionsDict1['3F'])} {VALFUNC(positionsDict1['3G'])} {VALFUNC(positionsDict1['3H'])} {VALFUNC(positionsDict1['3I'])} {VALFUNC(positionsDict1['3J'])}\t\t3 {VALFUNC(positionsDict2['3A'])} {VALFUNC(positionsDict2['3B'])} {VALFUNC(positionsDict2['3C'])} {VALFUNC(positionsDict2['3D'])} {VALFUNC(positionsDict2['3E'])} {VALFUNC(positionsDict2['3F'])} {VALFUNC(positionsDict2['3G'])} {VALFUNC(positionsDict2['3H'])} {VALFUNC(positionsDict2['3I'])} {VALFUNC(positionsDict2['3J'])}")
	print(f"4 {VALFUNC(positionsDict1['4A'])} {VALFUNC(positionsDict1['4B'])} {VALFUNC(positionsDict1['4C'])} {VALFUNC(positionsDict1['4D'])} {VALFUNC(positionsDict1['4E'])} {VALFUNC(positionsDict1['4F'])} {VALFUNC(positionsDict1['4G'])} {VALFUNC(positionsDict1['4H'])} {VALFUNC(positionsDict1['4I'])} {VALFUNC(positionsDict1['4J'])}\t\t4 {VALFUNC(positionsDict2['4A'])} {VALFUNC(positionsDict2['4B'])} {VALFUNC(positionsDict2['4C'])} {VALFUNC(positionsDict2['4D'])} {VALFUNC(positionsDict2['4E'])} {VALFUNC(positionsDict2['4F'])} {VALFUNC(positionsDict2['4G'])} {VALFUNC(positionsDict2['4H'])} {VALFUNC(positionsDict2['4I'])} {VALFUNC(positionsDict2['4J'])}")
	print(f"5 {VALFUNC(positionsDict1['5A'])} {VALFUNC(positionsDict1['5B'])} {VALFUNC(positionsDict1['5C'])} {VALFUNC(positionsDict1['5D'])} {VALFUNC(positionsDict1['5E'])} {VALFUNC(positionsDict1['5F'])} {VALFUNC(positionsDict1['5G'])} {VALFUNC(positionsDict1['5H'])} {VALFUNC(positionsDict1['5I'])} {VALFUNC(positionsDict1['5J'])}\t\t5 {VALFUNC(positionsDict2['5A'])} {VALFUNC(positionsDict2['5B'])} {VALFUNC(positionsDict2['5C'])} {VALFUNC(positionsDict2['5D'])} {VALFUNC(positionsDict2['5E'])} {VALFUNC(positionsDict2['5F'])} {VALFUNC(positionsDict2['5G'])} {VALFUNC(positionsDict2['5H'])} {VALFUNC(positionsDict2['5I'])} {VALFUNC(positionsDict2['5J'])}")
	print(f"6 {VALFUNC(positionsDict1['6A'])} {VALFUNC(positionsDict1['6B'])} {VALFUNC(positionsDict1['6C'])} {VALFUNC(positionsDict1['6D'])} {VALFUNC(positionsDict1['6E'])} {VALFUNC(positionsDict1['6F'])} {VALFUNC(positionsDict1['6G'])} {VALFUNC(positionsDict1['6H'])} {VALFUNC(positionsDict1['6I'])} {VALFUNC(positionsDict1['6J'])}\t\t6 {VALFUNC(positionsDict2['6A'])} {VALFUNC(positionsDict2['6B'])} {VALFUNC(positionsDict2['6C'])} {VALFUNC(positionsDict2['6D'])} {VALFUNC(positionsDict2['6E'])} {VALFUNC(positionsDict2['6F'])} {VALFUNC(positionsDict2['6G'])} {VALFUNC(positionsDict2['6H'])} {VALFUNC(positionsDict2['6I'])} {VALFUNC(positionsDict2['6J'])}")
	print(f"7 {VALFUNC(positionsDict1['7A'])} {VALFUNC(positionsDict1['7B'])} {VALFUNC(positionsDict1['7C'])} {VALFUNC(positionsDict1['7D'])} {VALFUNC(positionsDict1['7E'])} {VALFUNC(positionsDict1['7F'])} {VALFUNC(positionsDict1['7G'])} {VALFUNC(positionsDict1['7H'])} {VALFUNC(positionsDict1['7I'])} {VALFUNC(positionsDict1['7J'])}\t\t7 {VALFUNC(positionsDict2['7A'])} {VALFUNC(positionsDict2['7B'])} {VALFUNC(positionsDict2['7C'])} {VALFUNC(positionsDict2['7D'])} {VALFUNC(positionsDict2['7E'])} {VALFUNC(positionsDict2['7F'])} {VALFUNC(positionsDict2['7G'])} {VALFUNC(positionsDict2['7H'])} {VALFUNC(positionsDict2['7I'])} {VALFUNC(positionsDict2['7J'])}")
	print(f"8 {VALFUNC(positionsDict1['8A'])} {VALFUNC(positionsDict1['8B'])} {VALFUNC(positionsDict1['8C'])} {VALFUNC(positionsDict1['8D'])} {VALFUNC(positionsDict1['8E'])} {VALFUNC(positionsDict1['8F'])} {VALFUNC(positionsDict1['8G'])} {VALFUNC(positionsDict1['8H'])} {VALFUNC(positionsDict1['8I'])} {VALFUNC(positionsDict1['8J'])}\t\t8 {VALFUNC(positionsDict2['8A'])} {VALFUNC(positionsDict2['8B'])} {VALFUNC(positionsDict2['8C'])} {VALFUNC(positionsDict2['8D'])} {VALFUNC(positionsDict2['8E'])} {VALFUNC(positionsDict2['8F'])} {VALFUNC(positionsDict2['8G'])} {VALFUNC(positionsDict2['8H'])} {VALFUNC(positionsDict2['8I'])} {VALFUNC(positionsDict2['8J'])}")
	print(f"9 {VALFUNC(positionsDict1['9A'])} {VALFUNC(positionsDict1['9B'])} {VALFUNC(positionsDict1['9C'])} {VALFUNC(positionsDict1['9D'])} {VALFUNC(positionsDict1['9E'])} {VALFUNC(positionsDict1['9F'])} {VALFUNC(positionsDict1['9G'])} {VALFUNC(positionsDict1['9H'])} {VALFUNC(positionsDict1['9I'])} {VALFUNC(positionsDict1['9J'])}\t\t9 {VALFUNC(positionsDict2['9A'])} {VALFUNC(positionsDict2['9B'])} {VALFUNC(positionsDict2['9C'])} {VALFUNC(positionsDict2['9D'])} {VALFUNC(positionsDict2['9E'])} {VALFUNC(positionsDict2['9F'])} {VALFUNC(positionsDict2['9G'])} {VALFUNC(positionsDict2['9H'])} {VALFUNC(positionsDict2['9I'])} {VALFUNC(positionsDict2['9J'])}")
	print(f"10{VALFUNC(positionsDict1['10A'])} {VALFUNC(positionsDict1['10B'])} {VALFUNC(positionsDict1['10C'])} {VALFUNC(positionsDict1['10D'])} {VALFUNC(positionsDict1['10E'])} {VALFUNC(positionsDict1['10F'])} {VALFUNC(positionsDict1['10G'])} {VALFUNC(positionsDict1['10H'])} {VALFUNC(positionsDict1['10I'])} {VALFUNC(positionsDict1['10J'])}\t\t10{VALFUNC(positionsDict2['10A'])} {VALFUNC(positionsDict2['10B'])} {VALFUNC(positionsDict2['10C'])} {VALFUNC(positionsDict2['10D'])} {VALFUNC(positionsDict2['10E'])} {VALFUNC(positionsDict2['10F'])} {VALFUNC(positionsDict2['10G'])} {VALFUNC(positionsDict2['10H'])} {VALFUNC(positionsDict2['10I'])} {VALFUNC(positionsDict2['10J'])}")
	print()
	print(f"{Cinfo1[0]}\t\t\t\t{Cinfo2[0]}")
	print(f"{Binfo1[0]}\t\t\t\t{Binfo2[0]}")
	print(f"{Dinfo1[0]}\t\t\t\t{Dinfo2[0]}")
	print(f"{Sinfo1[0]}\t\t\t\t{Sinfo2[0]}")
	print(f"{Pinfo1[0]}\t\t\t{Pinfo2[0]}")
	print()
	print(f"Enter your move: {move[0]},{move[1]}")
	print()
	f.write(f"Round : {roundnum}\t\t\t\t\tGrid Size: 10x10\n\n")
	f.write(f"Player1's Hidden Board\t\tPlayer2's Hidden Board\n")
	f.write(f"  A B C D E F G H I J\t\t  A B C D E F G H I J\n")
	f.write(f"1 {VALFUNC(positionsDict1['1A'])} {VALFUNC(positionsDict1['1B'])} {VALFUNC(positionsDict1['1C'])} {VALFUNC(positionsDict1['1D'])} {VALFUNC(positionsDict1['1E'])} {VALFUNC(positionsDict1['1F'])} {VALFUNC(positionsDict1['1G'])} {VALFUNC(positionsDict1['1H'])} {VALFUNC(positionsDict1['1I'])} {VALFUNC(positionsDict1['1J'])}\t\t1 {VALFUNC(positionsDict2['1A'])} {VALFUNC(positionsDict2['1B'])} {VALFUNC(positionsDict2['1C'])} {VALFUNC(positionsDict2['1D'])} {VALFUNC(positionsDict2['1E'])} {VALFUNC(positionsDict2['1F'])} {VALFUNC(positionsDict2['1G'])} {VALFUNC(positionsDict2['1H'])} {VALFUNC(positionsDict2['1I'])} {VALFUNC(positionsDict2['1J'])}\n")
	f.write(f"2 {VALFUNC(positionsDict1['2A'])} {VALFUNC(positionsDict1['2B'])} {VALFUNC(positionsDict1['2C'])} {VALFUNC(positionsDict1['2D'])} {VALFUNC(positionsDict1['2E'])} {VALFUNC(positionsDict1['2F'])} {VALFUNC(positionsDict1['2G'])} {VALFUNC(positionsDict1['2H'])} {VALFUNC(positionsDict1['2I'])} {VALFUNC(positionsDict1['2J'])}\t\t2 {VALFUNC(positionsDict2['2A'])} {VALFUNC(positionsDict2['2B'])} {VALFUNC(positionsDict2['2C'])} {VALFUNC(positionsDict2['2D'])} {VALFUNC(positionsDict2['2E'])} {VALFUNC(positionsDict2['2F'])} {VALFUNC(positionsDict2['2G'])} {VALFUNC(positionsDict2['2H'])} {VALFUNC(positionsDict2['2I'])} {VALFUNC(positionsDict2['2J'])}\n")
	f.write(f"3 {VALFUNC(positionsDict1['3A'])} {VALFUNC(positionsDict1['3B'])} {VALFUNC(positionsDict1['3C'])} {VALFUNC(positionsDict1['3D'])} {VALFUNC(positionsDict1['3E'])} {VALFUNC(positionsDict1['3F'])} {VALFUNC(positionsDict1['3G'])} {VALFUNC(positionsDict1['3H'])} {VALFUNC(positionsDict1['3I'])} {VALFUNC(positionsDict1['3J'])}\t\t3 {VALFUNC(positionsDict2['3A'])} {VALFUNC(positionsDict2['3B'])} {VALFUNC(positionsDict2['3C'])} {VALFUNC(positionsDict2['3D'])} {VALFUNC(positionsDict2['3E'])} {VALFUNC(positionsDict2['3F'])} {VALFUNC(positionsDict2['3G'])} {VALFUNC(positionsDict2['3H'])} {VALFUNC(positionsDict2['3I'])} {VALFUNC(positionsDict2['3J'])}\n")
	f.write(f"4 {VALFUNC(positionsDict1['4A'])} {VALFUNC(positionsDict1['4B'])} {VALFUNC(positionsDict1['4C'])} {VALFUNC(positionsDict1['4D'])} {VALFUNC(positionsDict1['4E'])} {VALFUNC(positionsDict1['4F'])} {VALFUNC(positionsDict1['4G'])} {VALFUNC(positionsDict1['4H'])} {VALFUNC(positionsDict1['4I'])} {VALFUNC(positionsDict1['4J'])}\t\t4 {VALFUNC(positionsDict2['4A'])} {VALFUNC(positionsDict2['4B'])} {VALFUNC(positionsDict2['4C'])} {VALFUNC(positionsDict2['4D'])} {VALFUNC(positionsDict2['4E'])} {VALFUNC(positionsDict2['4F'])} {VALFUNC(positionsDict2['4G'])} {VALFUNC(positionsDict2['4H'])} {VALFUNC(positionsDict2['4I'])} {VALFUNC(positionsDict2['4J'])}\n")
	f.write(f"5 {VALFUNC(positionsDict1['5A'])} {VALFUNC(positionsDict1['5B'])} {VALFUNC(positionsDict1['5C'])} {VALFUNC(positionsDict1['5D'])} {VALFUNC(positionsDict1['5E'])} {VALFUNC(positionsDict1['5F'])} {VALFUNC(positionsDict1['5G'])} {VALFUNC(positionsDict1['5H'])} {VALFUNC(positionsDict1['5I'])} {VALFUNC(positionsDict1['5J'])}\t\t5 {VALFUNC(positionsDict2['5A'])} {VALFUNC(positionsDict2['5B'])} {VALFUNC(positionsDict2['5C'])} {VALFUNC(positionsDict2['5D'])} {VALFUNC(positionsDict2['5E'])} {VALFUNC(positionsDict2['5F'])} {VALFUNC(positionsDict2['5G'])} {VALFUNC(positionsDict2['5H'])} {VALFUNC(positionsDict2['5I'])} {VALFUNC(positionsDict2['5J'])}\n")
	f.write(f"6 {VALFUNC(positionsDict1['6A'])} {VALFUNC(positionsDict1['6B'])} {VALFUNC(positionsDict1['6C'])} {VALFUNC(positionsDict1['6D'])} {VALFUNC(positionsDict1['6E'])} {VALFUNC(positionsDict1['6F'])} {VALFUNC(positionsDict1['6G'])} {VALFUNC(positionsDict1['6H'])} {VALFUNC(positionsDict1['6I'])} {VALFUNC(positionsDict1['6J'])}\t\t6 {VALFUNC(positionsDict2['6A'])} {VALFUNC(positionsDict2['6B'])} {VALFUNC(positionsDict2['6C'])} {VALFUNC(positionsDict2['6D'])} {VALFUNC(positionsDict2['6E'])} {VALFUNC(positionsDict2['6F'])} {VALFUNC(positionsDict2['6G'])} {VALFUNC(positionsDict2['6H'])} {VALFUNC(positionsDict2['6I'])} {VALFUNC(positionsDict2['6J'])}\n")
	f.write(f"7 {VALFUNC(positionsDict1['7A'])} {VALFUNC(positionsDict1['7B'])} {VALFUNC(positionsDict1['7C'])} {VALFUNC(positionsDict1['7D'])} {VALFUNC(positionsDict1['7E'])} {VALFUNC(positionsDict1['7F'])} {VALFUNC(positionsDict1['7G'])} {VALFUNC(positionsDict1['7H'])} {VALFUNC(positionsDict1['7I'])} {VALFUNC(positionsDict1['7J'])}\t\t7 {VALFUNC(positionsDict2['7A'])} {VALFUNC(positionsDict2['7B'])} {VALFUNC(positionsDict2['7C'])} {VALFUNC(positionsDict2['7D'])} {VALFUNC(positionsDict2['7E'])} {VALFUNC(positionsDict2['7F'])} {VALFUNC(positionsDict2['7G'])} {VALFUNC(positionsDict2['7H'])} {VALFUNC(positionsDict2['7I'])} {VALFUNC(positionsDict2['7J'])}\n")
	f.write(f"8 {VALFUNC(positionsDict1['8A'])} {VALFUNC(positionsDict1['8B'])} {VALFUNC(positionsDict1['8C'])} {VALFUNC(positionsDict1['8D'])} {VALFUNC(positionsDict1['8E'])} {VALFUNC(positionsDict1['8F'])} {VALFUNC(positionsDict1['8G'])} {VALFUNC(positionsDict1['8H'])} {VALFUNC(positionsDict1['8I'])} {VALFUNC(positionsDict1['8J'])}\t\t8 {VALFUNC(positionsDict2['8A'])} {VALFUNC(positionsDict2['8B'])} {VALFUNC(positionsDict2['8C'])} {VALFUNC(positionsDict2['8D'])} {VALFUNC(positionsDict2['8E'])} {VALFUNC(positionsDict2['8F'])} {VALFUNC(positionsDict2['8G'])} {VALFUNC(positionsDict2['8H'])} {VALFUNC(positionsDict2['8I'])} {VALFUNC(positionsDict2['8J'])}\n")
	f.write(f"9 {VALFUNC(positionsDict1['9A'])} {VALFUNC(positionsDict1['9B'])} {VALFUNC(positionsDict1['9C'])} {VALFUNC(positionsDict1['9D'])} {VALFUNC(positionsDict1['9E'])} {VALFUNC(positionsDict1['9F'])} {VALFUNC(positionsDict1['9G'])} {VALFUNC(positionsDict1['9H'])} {VALFUNC(positionsDict1['9I'])} {VALFUNC(positionsDict1['9J'])}\t\t9 {VALFUNC(positionsDict2['9A'])} {VALFUNC(positionsDict2['9B'])} {VALFUNC(positionsDict2['9C'])} {VALFUNC(positionsDict2['9D'])} {VALFUNC(positionsDict2['9E'])} {VALFUNC(positionsDict2['9F'])} {VALFUNC(positionsDict2['9G'])} {VALFUNC(positionsDict2['9H'])} {VALFUNC(positionsDict2['9I'])} {VALFUNC(positionsDict2['9J'])}\n")
	f.write(f"10{VALFUNC(positionsDict1['10A'])} {VALFUNC(positionsDict1['10B'])} {VALFUNC(positionsDict1['10C'])} {VALFUNC(positionsDict1['10D'])} {VALFUNC(positionsDict1['10E'])} {VALFUNC(positionsDict1['10F'])} {VALFUNC(positionsDict1['10G'])} {VALFUNC(positionsDict1['10H'])} {VALFUNC(positionsDict1['10I'])} {VALFUNC(positionsDict1['10J'])}\t\t10{VALFUNC(positionsDict2['10A'])} {VALFUNC(positionsDict2['10B'])} {VALFUNC(positionsDict2['10C'])} {VALFUNC(positionsDict2['10D'])} {VALFUNC(positionsDict2['10E'])} {VALFUNC(positionsDict2['10F'])} {VALFUNC(positionsDict2['10G'])} {VALFUNC(positionsDict2['10H'])} {VALFUNC(positionsDict2['10I'])} {VALFUNC(positionsDict2['10J'])}\n\n")
	f.write(f"{Cinfo1[0]}\t\t\t\t{Cinfo2[0]}\n")
	f.write(f"{Binfo1[0]}\t\t\t\t{Binfo2[0]}\n")
	f.write(f"{Dinfo1[0]}\t\t\t\t{Dinfo2[0]}\n")
	f.write(f"{Sinfo1[0]}\t\t\t\t{Sinfo2[0]}\n")
	f.write(f"{Pinfo1[0]}\t\t\t{Pinfo2[0]}\n\n")
	f.write(f"Enter your move: {move[0]},{move[1]}\n\n")
	if turn == True: #If it is Player1's turn, process moves for Player2's data. Else process for Player1's data.
		processingMoves(positionsDict2,shipDict2,move,Cinfo2,Binfo2,Dinfo2,Sinfo2,Pinfo2)
	else: 
		processingMoves(positionsDict1,shipDict1,move,Cinfo1,Binfo1,Dinfo1,Sinfo1,Pinfo1)

def MovesLikeJagger():
	#Function to cull the herd.
	first_turn=True
	first_index = 0
	second_index = 0 
	while first_index < len(player1moves) or second_index < len(player2moves):
		try:	
			if first_turn:
				move = player1moves[first_index]
				move_check_list=move.split(",") #I split "move" string from comma because it helps me to cull the herd easily.
			else:
				move = player2moves[second_index]
				move_check_list=move.split(",")
		except:
			break
		else:	
			try:
				if len(move_check_list) <2:
					raise IndexError
				if move_check_list[0]=="":
					raise IndexError
				if move_check_list[1]=="":
					raise IndexError
				if len(move_check_list)>2:
					raise ValueError
				int(move_check_list[0])
				str(move_check_list[1])
				assert int(move_check_list[0])<=10
				assert lettoint(move_check_list[1])<=lettoint("J")
			except ValueError:
				print(f"ValueError: {move} is an unvalid move. Please enter a valid move!")
				if first_turn:
					first_index +=1
				else:
					second_index += 1
			except IndexError:
				print(f"IndexError: {move} is an unvalid move. Please enter a valid move!")
				if first_turn:
					first_index +=1
				else:
					second_index += 1
			except AssertionError:
				print(f"AssertionError: {move} is an unvalid move. Please enter a valid move!")
				if first_turn:
					first_index +=1
				else:
					second_index += 1
			except:
				print("kaBOOM: run for your life")
			else:
				move=move.replace(",","") #2,A ---> 2A
				PRINT_IT(move,first_turn)
				if first_turn:
					first_turn = False
					first_index +=1
				else:
					first_turn = True
					second_index += 1

#Global Variables 
with open("Battleship.out", "w") as f:
	filenames=""
	control=0
	try:
		player1txt=sys.argv[1]
		player2txt=sys.argv[2]
		player1in=sys.argv[3]
		player2in=sys.argv[4]
	except:
		print("You entered less command than expected.!")
		f.write("You entered less command than expected.!\n")
	else:
		try:
			positionsDict1=shipsPOSITIONS(readTXT(player1txt)) 
			shipDict1=finalshipdict(readTXT("OptionalPlayer1.txt"),positionsDict1)
		except:
			filenames+=player1txt+", "
			control+=1
		try:
			positionsDict2=shipsPOSITIONS(readTXT(player2txt))
			shipDict2=finalshipdict(readTXT("OptionalPlayer2.txt"),positionsDict2)
		except:
			filenames+=player2txt+", "
			control+=1
		try:	
			player1moves=playerXmoves(readTXT(player1in))
		except:
			filenames+=player1in+", "
			control+=1
		try:	
			player2moves=playerXmoves(readTXT(player2in))
		except:
			filenames+=player2in+", "
			control+=1
		else:
			roundnum=0 
			#I have used lists for checking ship's infos because when you change the list in a function(local), it changes in global too.
			Cinfo1=["Carrier\t\t-"]
			Binfo1=["Battleship\t- -"]
			Dinfo1=["Destroyer\t-"]
			Sinfo1=["Submarine\t-"]
			Pinfo1=["Patrol Boat\t- - - -"]
			Cinfo2=["Carrier\t\t-"]
			Binfo2=["Battleship\t- -"]
			Dinfo2=["Destroyer\t-"]
			Sinfo2=["Submarine\t-"]
			Pinfo2=["Patrol Boat\t- - - -"]
			Cinfocheck=["Carrier\t\tX"]
			Binfocheck=["Battleship\tX X"]
			Dinfocheck=["Destroyer\tX"]
			Sinfocheck=["Submarine\tX"]
			Pinfocheck=["Patrol Boat\tX X X X"]
			print("Battle of Ships Game")
			print()
			f.write("Battle of Ships Game\n\n")
			MovesLikeJagger()
			if Cinfo1==Cinfocheck and Binfo1==Binfocheck and Dinfo1==Dinfocheck and Sinfo1==Sinfocheck and Pinfo1==Pinfocheck and Cinfo2==Cinfocheck and Binfo2==Binfocheck and Dinfo2==Dinfocheck and Sinfo2==Sinfocheck and Pinfo2==Pinfocheck:
				print("It is a Draw!")
				printFINALINFO()
				f.write("It is a Draw!\n\n")
				writeFINALINFO()
			elif Cinfo1==Cinfocheck and Binfo1==Binfocheck and Dinfo1==Dinfocheck and Sinfo1==Sinfocheck and Pinfo1==Pinfocheck:
				print("Player2 Wins!")
				printFINALINFO()
				f.write("Player2 Wins!\n\n")
				writeFINALINFO()
			elif Cinfo2==Cinfocheck and Binfo2==Binfocheck and Dinfo2==Dinfocheck and Sinfo2==Sinfocheck and Pinfo2==Pinfocheck:
				print("Player1 Wins!")
				printFINALINFO()
				f.write("Player1 Wins!\n\n")
				writeFINALINFO()
		if control==1:
			print(f"IOError: input file {filenames[:-2]} is not reachable.")
			f.write(f"IOError: input file {filenames[:-2]} is not reachable.")
		if control>1:
			print(f"IOError: input files {filenames[:-2]} are not reachable.")
			f.write(f"IOError: input files {filenames[:-2]} are not reachable.")

'''
AYKUT ALP GURLER 
B2210356024
'''