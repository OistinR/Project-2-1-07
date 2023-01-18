# import tensorflow as tf
#
# print(tf.__version__)
import csv
preMoves = []
postMoves = []

# opening the CSV file
def read_Data():
    with open("C:/Users/Oistín/Documents/Github/Project-2-1-07/core/src/com/mygdx/trainingData.csv", mode='r') as file:
        # reading the CSV file
        csvFile = csv.reader(file)
        # displaying the contents of the CSV file
        count = 100


        pregame = []
        postgame = []
        flag = False
        print("please wait...")
        for lines in csvFile:
            for num in lines:
                if flag and num == "999.0":
                    preMoves.insert(preMoves.__sizeof__(), [pregame.copy()])
                    pregame.clear()
                    flag = False
                elif (not flag) and num == "999.0":
                    postMoves.insert(postMoves.__sizeof__(), [postgame.copy()])
                    postgame.clear()
                    flag = True
                elif flag:
                    # print(pregame)
                    pregame.insert(pregame.__sizeof__(), float(num))
                else:
                    postgame.insert(postgame.__sizeof__(), float(num))
        del postMoves[0]

        print("read successful")




def duplicateRemover(list1, list2):
    if list1.__sizeof__()!=list2.__sizeof__():
        print("sizes are not equal")
        return -1

    i = 0

    while i < list1.__sizeof__():
        j = 0
        while j < list1[i].__sizeof__():
            k = 0
            while k < list1[i][j].__sizeof__():
                if list1[i][j][k] == list2[i][j][k]:
                    list2[i][j][k] = 0.0

    return list2


def convertToFloat(listStr):#this was a java implementation transfered over, it took like minutes for 10 game
    i = 0
    while i < listStr.__sizeof__():
        j = 0
        while j < listStr[i].__sizeof__():
            k = 0
            while k < listStr[i][j].__sizeof__():
                listStr[i][j][k] = float(listStr[i][j][k])


#preMoves = []
#postMoves = []

read_Data()
# convertToFloat(preMoves)
# convertToFloat(postMoves)
print(postMoves[0])
duplicateRemover(preMoves,postMoves)
print(postMoves[1])
