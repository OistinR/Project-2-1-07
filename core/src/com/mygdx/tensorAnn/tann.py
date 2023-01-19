import tensorflow as tf
from sklearn.model_selection import train_test_split
from tensorflow.python.keras import Sequential
from tensorflow.python.keras.layers import Dense, Flatten
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


def training():
    read_Data()
    #preMoves = []
    #postMoves = []

    # X_train, X_test, Y_train, Y_test = train_test_split(preMoves, postMoves)
    #
    # X_train.head()
    # # Y_train.head()
    # print(len(preMoves[0]))
    # print(len(preMoves[0][0]))
    X = np.asarray(preMoves.copy())

    Y = np.asarray(postMoves.copy())

    # X_train, X_test, Y_train, Y_split = train_test_split(X,Y, test_size=0.2)
    # ? this needed?

    # for list in preMoves:
    #     print(len(list))
    #     for list2 in list:
    #         print(list2)

    model = Sequential()
    model.add(Flatten(input_shape = (19,)))
    model.add(Dense(units=20, activation='softmax'))
    model.add(Dense(units=20, activation='softmax'))
    model.add(Dense(units=20, activation='tanh'))
    model.add(Dense(19, activation='tanh'))
    model.compile(loss='binary_crossentropy', optimizer="Adamax", metrics='accuracy')

    model.fit(X,Y, epochs=100, batch_size=100)


    model.save('C:/Users/Oistín/Documents/Github/Project-2-1-07/core/src/com/mygdx/tensorAnn/model1')


def main():
    # read_Data()
    training()
    new_model = tf.keras.models.load_model('C:/Users/Oistín/Documents/Github/Project-2-1-07/core/src/com/mygdx/tensorAnn/model1')
    X = np.asarray(preMoves.copy())
    Y = np.asarray(postMoves.copy())
    predict = np.asarray(new_model.predict(X[0]))


    maxindex = np.max(predict)
    minindex = np.min(predict)

    print(predict)
    print(Y[0])

    print("best index red:"+str(maxindex))
    print("best index blue:"+str(minindex))
    print("best index red:"+str(Y[0][0][predict.argmax()]))
    print("best index blue:"+str(Y[0][0][predict.argmin()]))

main()
