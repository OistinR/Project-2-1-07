import tensorflow as tf
from sklearn.model_selection import train_test_split
from tensorflow.python.keras import Sequential
from tensorflow.python.keras.layers import Dense, Flatten
import numpy as np
from sklearn.metrics import accuracy_score
import winsound


print(tf.__version__)
import csv
preMoves = []
postMoves = []

# opening the CSV file
def read_Data():
    with open("C:/Users/Oistín/Documents/Github/Project-2-1-07/core/src/trainingData2.csv", mode='r') as file:
        # reading the CSV file
        csvFile = csv.reader(file)
        # displaying the contents of the CSV file

        pregame = []
        postgame = []
        flag = False
        print("please wait...")
        for lines in csvFile:
            for num in lines:
                if flag and num == "999.0":
                    if(len(pregame)!=0):
                        preMoves.insert(preMoves.__sizeof__(), pregame.copy())
                    pregame.clear()
                    flag = False
                elif (not flag) and num == "999.0":
                    if(len(postgame)!=0):
                        postMoves.insert(postMoves.__sizeof__(), postgame.copy())
                    postgame.clear()
                    flag = True
                elif flag:
                    # print(pregame)
                    pregame.insert(pregame.__sizeof__(), float(num))
                else:
                    postgame.insert(postgame.__sizeof__(), abs(float(num)))
        print("read successful")


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

    # model = Sequential()
    # model.add(Dense(units=38, activation= 'relu', input_dim = 38))
    # model.add(Dense(units=220, activation='relu'))
    # model.add(Dense(units=120, activation='relu')) # 96% relu relu softmax
    # model.add(Dense(37, activation='softmax')) #96.8 tan tan  tan sofmax 0.05? on test data
    #
    # model.compile(loss='binary_crossentropy', optimizer="Adam", metrics='accuracy')
    model = tf.keras.models.load_model('C:/Users/Oistín/Documents/Github/Project-2-1-07/core/src/com/mygdx/tensorAnn/model1')

    model.fit(X,Y, epochs=100, batch_size=32)#batch = 5000 games for 100 epoch and 32 batch is 15min+

    model.save('C:/Users/Oistín/Documents/Github/Project-2-1-07/core/src/com/mygdx/tensorAnn/model1')
    # print(model.evaluate(preMoves,postMoves))
    winsound.Beep(440, 500)

def main():

    read_Data()
    # print(len(preMoves))
    # print(len(postMoves))
    # print(len(preMoves[0]))
    # print(len(postMoves[0]))
    # print(preMoves)
    # # print(postMoves)
    # training()
    # winsound.Beep(440, 500)
    new_model = tf.keras.models.load_model('C:/Users/Oistín/Documents/Github/Project-2-1-07/core/src/com/mygdx/tensorAnn/model1')
    print(new_model.evaluate(preMoves, postMoves))

    # print(preMoves[0])
    # X = np.asarray(preMoves.copy())
    # Y = np.asarray(postMoves.copy())
    # predict: object = new_model.predict(preMoves)
    # j = 0
    #
    # for array in predict:
    #     array[array.argmax()] = 1.0
    #     i = 0
    #     while i< len(array):
    #         if array[i] < 1.0:
    #             data = 0.0
    #
    # print(predict[0])
    # print(predict[0].argmax())
    # print(predict[0])
    # winsound.Beep(440, 500)
    # print(accuracy_score(postMoves, predict))
    # # maxindex = np.max(predict)

    # print(predict)
    # print(postMoves[0])

    # print("best index red:"+str(maxindex))
    # print("best index red:"+str(postMoves[0][predict.argmax()]))
    #



main()
