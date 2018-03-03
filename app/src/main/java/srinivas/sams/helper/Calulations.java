package srinivas.sams.helper;

import java.text.DecimalFormat;

/**
 * Created by SantoshT on 2/23/2017.
 */

public class Calulations {

    public static double mm=0.16;//1dp=0.16mm
    static DecimalFormat df = new DecimalFormat("#.00");
   /* function mm_to_feets(number){
        number = number*0.00328084;
        number = Math.floor(number);
        return number;
    }*/

    public static double mm_to_feet(double number)
    {
        number = number*0.00328084;
        number = Math.floor(number);
        return number;
    }
    /*function cm_to_feets(number){
        console.log(number/30.48);
        number = number * 0.3937008;
        number = number/12;
        number = Math.floor(number);
        return number;
    }*/

    public static double cm_to_feet(double number)
    {
        number = number * 0.3937008;
        number = number/12;
        number = Math.floor(number);
        return number;
    }

   /* function feet_to_inches(number){
        var remainder = parseFloat(number - Math.floor(number)).toFixed(3);
        var number = parseFloat(remainder*12).toFixed(2);
        return number;
    }*/

    public static double feet_to_inch(double number)
    {
        double remainder =  number - Math.floor(number);
         number =  (remainder*12);
        df.format(number);
        return number;
    }


  /*  function m_to_feets(number){

        number = number * 39.37007874;
        number = number/12;
        number = Math.floor(number);
        //console.log(number);
        return number;
    }*/

    public static double m_to_feets(double number)
    {
        number = number * 39.37007874;
        number = number/12;
        number = Math.floor(number);
        return number;
    }

   /* function m_feet_to_inches(number){

        number = number * 39.37007874;
        number = number/12;
        number = parseFloat(number).toFixed(3);
        var remainder = parseFloat(number - Math.floor(number)).toFixed(3);
        remainder = parseFloat(remainder*12).toFixed(2);
        console.log(remainder);
        return remainder;
    }*/

    public static double m_feet_to_inches(double number)
    {


        number = number * 39.37007874;
       number = number/12;
        double remainder =  number - Math.floor(number);
        remainder =  remainder*12;
        return round(remainder,2);
    }

   /* function mm_cm_m_feet_to_inches(number){

        number = number * 0.3937008;
        number = number/12;
        number = parseFloat(number).toFixed(3);
        console.log(number);
        var remainder = parseFloat(number - Math.floor(number)).toFixed(3);
        number = parseFloat(remainder*12).toFixed(2);
        console.log(remainder);
        return number;
    }*/


    public static double mm_cm_m_feet_to_inches(double number)
    {
        number = number * 0.3937008;
        number = number/12;
        double remainder =  number - Math.floor(number);
        remainder =  remainder*12;
        return round(remainder,2);
    }

   /* function feet_to_feet(number){
        number = Math.floor(number);
        return number;
    }*/


    public static double feet_to_feet(double number)
    {
        number = Math.floor(number);
        return number;
    }

   /* function mm_feet_to_inches(number){

        var number = number*(0.00328084);

        var remainder = parseFloat(number - Math.floor(number)).toFixed(3);

        var number = parseFloat(remainder*12).toFixed(2);
        console.log(number);
        return number;
    }*/

    public static double mm_feet_to_inches(double number)
    {
        number = number*(0.00328084);
        double remainder=number-Math.floor(number);
       /* double remainder =  number - Math.floor(number);*/
        number =  remainder*12;
        return round(number,2);
    }

    /*function inch_to_feets(number){
        number = number*(0.0833333);
        number = Math.floor(number);
        return number;
    }
*/
    public static double inch_to_feets(double number)
    {
        /*number = number*(0.0833333);
        number = Math.floor(number);*/
        number = number / 12;
        number = Math.floor(number);
        return number;
    }

   /* function inch_to_inches(number){
        number = number*(0.0833333);
        var remainder = parseFloat(number - Math.floor(number)).toFixed(3);
        var number = parseFloat(remainder*12).toFixed(2);
        return number;
    }*/


    public static double inch_to_inches(double number)
    {
        number = number*(0.0833333);
        double remainder =  number - Math.floor(number);
        remainder =  remainder*12;
      //  number = number%12;
        return round(remainder,2);
    }
  /*  function feet_to_inches(number){
        var remainder = parseFloat(number - Math.floor(number)).toFixed(3);
        var number = parseFloat(remainder*12).toFixed(2);
        return number;
    }*/

    public static double feet_to_inches(double number)
    {
        double remainder = number - Math.floor(number);
        number =  remainder*12;
        return round(number,2);
    }

   /* function m_feet_to_inches(number){

        number = number * 39.37007874;
        number = number/12;
        number = parseFloat(number).toFixed(3);
        var remainder = parseFloat(number - Math.floor(number)).toFixed(3);
        remainder = parseFloat(remainder*12).toFixed(2);
        console.log(remainder);
        return remainder;
    }*/

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
