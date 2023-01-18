import tensorflow as tf
from sklearn.model_selection import train_test_split
from tensorflow.python.keras import Sequential
from tensorflow.python.keras.layers import Dense
import numpy as np

print(tf.__version__)
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
                    if(len(pregame)!=0):
                        preMoves.insert(preMoves.__sizeof__(), [pregame.copy()])
                    pregame.clear()
                    flag = False
                elif (not flag) and num == "999.0":
                    if(len(postgame)!=0):
                        postMoves.insert(postMoves.__sizeof__(), [postgame.copy()])
                    postgame.clear()
                    flag = True
                elif flag:
                    # print(pregame)
                    pregame.insert(pregame.__sizeof__(), float(num))
                else:
                    postgame.insert(postgame.__sizeof__(), float(num))
         # ? I know why this happens but im not going to tell you
        print("read successful")
        # del postMoves[0]


read_Data()
#preMoves = []
#postMoves = []

# X_train, X_test, Y_train, Y_test = train_test_split(preMoves, postMoves)
#
# X_train.head()
# # Y_train.head()
# print(len(preMoves[0]))
# print(len(preMoves[0][0]))
X = np.array(preMoves.copy());
Y = np.array(postMoves.copy());

X_train, X_test, Y_train, Y_split = train_test_split(X,Y, test_size=0.2)
# ? this needed?

# for list in preMoves:
#     print(len(list))
#     for list2 in list:
#         print(list2)

model = Sequential()
model.add(Dense(units=19,activation='relu',input_dim=19))
model.add(Dense(units=64,activation='relu'))
model.add(Dense(units=19,activation='sigmoid'))
model.compile(loss='binary_crossentropy', optimizer='sgd', metrics='accuracy')
i = 0
while i< len(X_train):
    model.fit(X_train[i],Y_train[i],epochs=1, batch_size=1)
