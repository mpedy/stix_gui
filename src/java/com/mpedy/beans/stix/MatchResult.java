/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mpedy.beans.stix;

/**
 *
 * @author cuore
 */
public class MatchResult {

    public Float comparisons = 0.0f;
    public Float result = 0.0f;

    public MatchResult() {
    }

    public MatchResult(Float result, Float comparison) {
        this.result = result;
        this.comparisons = comparison;
    }

    @Override
    public String toString() {
        return "MatchResult{" + "comparisons=" + comparisons + ", result=" + result + '}';
    }

    public Float getAsFloat() {
        if (this.comparisons == 0.0f || this.result <= 0.0f) {
            return 0.0f;
        }
        return this.result * 100 / this.comparisons;
    }

    public void add(Float res) {
        this.result += res;
        this.comparisons += 1.0f;
    }

    public void add(MatchResult res) {
        this.comparisons += res.comparisons;
        this.result += res.result;
    }

    public Float getComparisons() {
        return comparisons;
    }

    public void setComparisons(Float comparisons) {
        this.comparisons = comparisons;
    }

    public Float getResult() {
        return result;
    }

    public void setResult(Float result) {
        this.result = result;
    }

}
