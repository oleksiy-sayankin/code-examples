package com.javasensei.portfolio.deepforest;

import com.javasensei.portfolio.math.MathHelper;

/**
 * @author oleksiy sayankin
 */
public class Sin implements Comparable{
    private double sinValue;
    private Quadrant quadrant;

    public double getSinValue() {
        return sinValue;
    }

    public void setSinValue(double sinValue) {
        this.sinValue = sinValue;
    }

    public Quadrant getQuadrant() {
        return quadrant;
    }

    public void setQuadrant(Quadrant quadrant) {
        this.quadrant = quadrant;
    }

    public Sin(Quadrant quadrant){
        switch (quadrant){
            case FIRST:
            case SECOND:
            case FIFTH:
            case SIXTH:
                sinValue = 0.5;
                break;
            case THIRD:
            case FORTH:
            case SEVENTH:
            case EIGHT:
                sinValue = -0.5;
                break;
        }
        this.quadrant = quadrant;
    }

    public Sin(double sinValue, Quadrant quadrant){
        if(Math.abs(sinValue)> 1){
            throw new IllegalArgumentException("Absolute value of sin must be less or equal to 1. sin = " + sinValue);
        }
        switch (quadrant){
            case FIRST:
            case SECOND:
            case FIFTH:
            case SIXTH:
                if(sinValue < 0){
                    throw new IllegalArgumentException("Sign of sin must be positive in 1t,2nd, 5th and 6th quadrants. sin = "
                            + sinValue + ", quadrant = " + quadrant);
                }
                break;
            case THIRD:
            case FORTH:
            case SEVENTH:
            case EIGHT:
                if(sinValue > 0){
                    throw new IllegalArgumentException("Sign of sin must be negative in 3d, 4th, 7th and 8th quadrants. sin = "
                            + sinValue + ", quadrant = " + quadrant);
                }
                break;
        }
        this.sinValue = sinValue;
        this.quadrant = quadrant;
    }

    @Override
    public int compareTo(Object other) {
        Sin otherSin = (Sin)other;
        if(this.quadrant!= otherSin.getQuadrant()){
           return this.quadrant.compareTo(otherSin.getQuadrant());
        }

        switch (this.quadrant){
            case FIRST:
            case FORTH:
            case FIFTH:
            case EIGHT:
                return (int)Math.signum(this.sinValue - otherSin.sinValue);
            case SECOND:
            case THIRD:
            case SIXTH:
            case SEVENTH:
                return (int)Math.signum(otherSin.sinValue - this.sinValue);

        }
        return 0;
    }

    public Sin normalizedSin(){
        if(Quadrant.isNormal(quadrant)){
            return this;
        }
        Quadrant normalizedQuadrant = quadrant;
        switch (this.quadrant){
            case FIFTH:
                normalizedQuadrant = Quadrant.FIRST;
                break;
            case SIXTH:
                normalizedQuadrant = Quadrant.SECOND;
                break;
            case SEVENTH:
                normalizedQuadrant = Quadrant.THIRD;
                break;
            case EIGHT:
                normalizedQuadrant = Quadrant.FORTH;
                break;
        }

        return new Sin(sinValue, normalizedQuadrant);
    }

    @Override
    public String toString(){
        return "[ val = " + sinValue + ", Q = " + quadrant + " ]";
    }

    @Override
    public boolean equals(Object other){
        if(other == null){
            return false;
        }
        if(this == other){
            return true;
        }
        if(!(other instanceof Sin)){
            return false;
        }
        Sin otherSin = (Sin)other;
        return MathHelper.equalsZero(this.sinValue - otherSin.sinValue) && this.quadrant == otherSin.quadrant;
    }

    @Override
    public int hashCode(){
        return (int)(Math.round(sinValue * 100)) + quadrant.hashCode();
    }
}
