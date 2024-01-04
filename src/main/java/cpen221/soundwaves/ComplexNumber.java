package cpen221.soundwaves;

public class ComplexNumber {

    /**
     * Real part of the complex number.
     */
    private final double real;

    /**
     * Imaginary part of the complex number.
     */
    private final double img;

    // Representation Invariants:
    //  real and img can only take valid double values.

    // Abstraction Functions:
    //  ComplexNumber object represents complex number with a real part (real)
    //  and an imaginary part (img).


    /**
     * Z = a + bi.
     *
     * @param real the real part of the complex number (a).
     * @param img the imaginary part of the complex number (b).
     */
    public ComplexNumber(double real, double img) {
        this.real = real;
        this.img = img;
    }

    /**
     * e^(ix).
     *
     * @param theta the real value 'e' is raised to.
     * @return the complex number of the form (cos(theta) - i*sin(theta)).
     */
    public ComplexNumber eulerRepresentation(double theta) {
        double cosinePart = Math.cos(theta);
        double sinePart = (Math.sin(theta));
        return new ComplexNumber(cosinePart, sinePart);
    }

    /**
     * Multiplies a scalar value to a complex number.
     *
     * @param scalar the value to be multiplied to the complex number.
     * @return the complex number multiplied with the scalar.
     */

    public ComplexNumber multiply(double scalar) {
        double realPart = scalar * (this.real);
        double imgPart = scalar * (this.img);
        return new ComplexNumber(realPart, imgPart);
    }

    /**
     * Adds two complex numbers.
     *
     * @param other the complex number which must be added.
     * @return the sum of complex numbers.
     */
    public ComplexNumber add(ComplexNumber other) {
        double realPart = this.real + other.real;
        double imgPart = this.img + other.img;
        return new ComplexNumber(realPart, imgPart);
    }

    /**
     * Complex Number: Z = a + bi
     * The magnitude of a complex number is sqrt(a^2 + b^2).
     *
     * @return the magnitude of the complex number.
     */
    public double toReal() {
        return Math.sqrt(Math.pow(this.real, 2) + Math.pow(this.img, 2));
    }

    /**
     * Compares 2 complex numbers.
     *
     * @param num1 the first complex number.
     * @param num2 the second complex number.
     * @return the biggest of the 2 complex numbers.
     */
    public static ComplexNumber max(ComplexNumber num1, ComplexNumber num2) {
        double magnitude1 = Math.sqrt(Math.pow(num1.real, 2) + Math.pow(num1.img, 2));
        double magnitude2 = Math.sqrt(Math.pow(num2.real, 2) + Math.pow(num2.img, 2));

        if (magnitude1 >= magnitude2) {
            return num1;
        } else {
            return num2;
        }
    }

}
