import tensorflow as tf
from sklearn.model_selection import train_test_split
from tensorflow.python.keras.optimizers import adam_v2
from tensorflow.python.keras import Sequential
from tensorflow.python.keras.layers import Dense
import numpy as np
from sklearn.metrics import accuracy_score
import winsound
import matplotlib.pyplot as plt
from matplotlib import rcParams




print(tf.__version__)
import csv
preMoves = []
postMoves = []
preMR = []
postMR = []

# opening the CSV file and coverts it into features and labels
def read_Data():
    with open("core/src/trainingData3.csv", mode='r') as file:
        # reading the CSV file
        csvFile = csv.reader(file)

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
                    pregame.insert(pregame.__sizeof__(), float(num))
                else:
                    postgame.insert(postgame.__sizeof__(), abs(float(num)))
        print("read successful")



# used for training, readData() must be ran first it can intialise or update a network and runs it
# see tensor flow keras models.

def training():

    X = np.asarray(preMR.copy())
    Y = np.asarray(postMR.copy())

    model = Sequential()
    model.add(Dense(units=2000, activation= 'relu', input_dim = 38))
    model.add(Dense(units=1000, activation= 'relu'))
    model.add(Dense(37, activation='softmax')) ##this may not reflect the final model
    model.compile(loss='binary_crossentropy', optimizer='Adam', metrics='accuracy')

    # model = tf.keras.models.load_model('core/src/com/mygdx/tensorAnn/model47')

    inital_history = model.fit(X,Y, epochs=5, batch_size=32) ## trains the data

    model.save('C:/Users/Oistín/Documents/Github/Project-2-1-07/core/src/com/mygdx/tensorAnn/model47')
    winsound.Beep(440, 500)


def main():
    read_Data()

    # training()

    new_model = tf.keras.models.load_model('C:/Users/Oistín/Documents/Github/Project-2-1-07/core/src/com/mygdx/tensorAnn/model47')
    print(new_model.output_names)



main()
