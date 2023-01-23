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

# opening the CSV file
def read_Data():
    with open("C:/Users/Oistín/Documents/Github/Project-2-1-07/core/src/trainingData3.csv", mode='r') as file:
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
                    pregame.insert(pregame.__sizeof__(), float(num))
                else:
                    postgame.insert(postgame.__sizeof__(), abs(float(num)))
        print("read successful")
        # preM = [array for array in preMoves if array[-1]==1.0]
        i = 0
        k = len(preMoves)

        for array in preMoves:
            if(i%2 == 0): #red
                preMR.insert(preMR.__sizeof__(),array)
            i= i +1#
        print(preMR[0])

        for array in postMoves:
            if(i%2 == 0): #red
                postMR.insert(postMR.__sizeof__(),array)
            i= i +1
        print(postMR[0])
        print("data processing compelet.")




def training():

    X = np.asarray(preMR.copy())
    Y = np.asarray(postMR.copy())

    model = Sequential()
    model.add(Dense(units=2000, activation= 'relu', input_dim = 38))
    model.add(Dense(units=1000, activation= 'relu'))
    model.add(Dense(37, activation='softmax')) #96.8 tan tan  tan sofmax 0.05? on test data
    model.compile(loss='binary_crossentropy', optimizer='Adam', metrics='accuracy')

    # model = tf.keras.models.load_model('C:/Users/Oistín/Documents/Github/Project-2-1-07/core/src/com/mygdx/tensorAnn/model5')

    inital_history = model.fit(X,Y, epochs=5, batch_size=32) #batch = 5000 games for 100 epoch and 32 batch is 15min+

    rcParams['figure.figsize'] = (18, 8)
    rcParams['axes.spines.top'] = False
    rcParams['axes.spines.right'] = False
    plt.plot(np.arange(1, 6), inital_history.history['loss'], label='Loss', lw=3)
    plt.plot(np.arange(1, 6), inital_history.history['accuracy'], label='Accuracy', lw=3)
    plt.title('Evaluation metrics', size=20)
    plt.xlabel('Epoch', size=5)
    plt.legend()
    plt.savefig('lossvsacceval_vs_lr.jpg', dpi=300, bbox_inches='tight');

    model.save('C:/Users/Oistín/Documents/Github/Project-2-1-07/core/src/com/mygdx/tensorAnn/model5')

    # print(model.evaluate(preMoves,postMoves))
    winsound.Beep(440, 500)


def main():
    read_Data()
    winsound.Beep(440, 500)
    # print(len(preMoves))
    # print(len(postMoves))
    # print(len(preMoves[0]))
    # print(len(postMoves[0]))
    # print(preMoves)
    # print(postMoves)
    # training()
    # winsound.Beep(440, 200)

    new_model = tf.keras.models.load_model('C:/Users/Oistín/Documents/Github/Project-2-1-07/core/src/com/mygdx/tensorAnn/model47')
    print(new_model.output_names)

# config = new_model.get_config();
    # # config['']
    # print(config);
    # new_model.layers[0]._name = "input"
    # new_model.layers[-1]._name = "output"


    # print(new_model.evaluate(preMoves, postMoves))

    # winsound.Beep(440, 500)

    # i = 0
    # while i<10:
    #     predict  = new_model.predict([preMoves[i]])
    #     print(predict[0])
    #     print(predict[0].argmax())
    #     print(postMoves[i])
    #     print(postMoves[i][predict[0].argmax()])
    #     i+=1

    # winsound.Beep(440, 500)
    # print(accuracy_score(postMoves, predict))
    # maxindex = np.max(predict)

    # print(predict)
    # print(postMoves[0])

    # print("best index red:"+str(maxindex))
    # print("best index red:"+str(postMoves[0][predict.argmax()]))



main()
