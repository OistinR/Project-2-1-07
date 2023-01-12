package com.mygdx.game.bots.MCST.Learning;

public interface IActivationFunction {
    double output(double x);

    double outputDerivative(double x);
}
