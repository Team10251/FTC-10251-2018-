package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import java.util.Locale;

/**
 * Created by Hirsh Ramani on 1/20/2018.
 */
@Autonomous(name = "YulianHitsTheBlueJuul", group = "Sensor")
public class yulianHitsTheJuulBlue extends LinearOpMode {
    ColorSensor sensorColor;
    DistanceSensor sensorDistance;
    Boolean inLoop;
    Servo armServo;
    Servo jewelHitter;


    @Override
    public void runOpMode() {
        armServo = hardwareMap.servo.get("armServo");
        jewelHitter = hardwareMap.servo.get("jewelHitter");

        // get a reference to the color sensor.
        sensorColor = hardwareMap.get(ColorSensor.class, "communismcolor");


        // get a reference to the distance sensor that shares the same name.
        sensorDistance = hardwareMap.get(DistanceSensor.class, "communismcolor");


        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F, 0F, 0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // sometimes it helps to multiply the raw RGB values with a scale factor
        // to amplify/attentuate the measured values.
        final double SCALE_FACTOR = 255;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        int relativeLayoutId = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "id", hardwareMap.appContext.getPackageName());
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutId);

        // wait for the start button to be pressed.
        waitForStart();
       /* jewelHitter.setPosition(0);
        armServo.setPosition(0);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //bring down arm
        jewelHitter.setPosition(.5);
        armServo.setPosition(.5);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // loop and read the RGB and distance data.
        // Note we use opModeIsActive() as our loop condition because it is an interruptible method.
        sensorColor.enableLed(false);
        // convert the RGB values to HSV values.
        // multiply by the SCALE_FACTOR.
        // then cast it back to int (SCALE_FACTOR is a double)
        Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
                (int) (sensorColor.green() * SCALE_FACTOR),
                (int) (sensorColor.blue() * SCALE_FACTOR),
                hsvValues);
        // send the info back to driver station using telemetry function.
        //zero the distance sensor
        double distance = sensorDistance.getDistance(DistanceUnit.CM) - 5.45;

        telemetry.addData("Distance (cm)",
                String.format(Locale.US, "%.02f", distance));
        telemetry.addData("Alpha", sensorColor.alpha());
        telemetry.addData("Red  ", sensorColor.red());
        telemetry.addData("Green", sensorColor.green());
        telemetry.addData("Blue ", sensorColor.blue());
        telemetry.addData("Hue", hsvValues[0]);

        if (sensorColor.red() > sensorColor.blue() && sensorColor.red() > sensorColor.green()) {
            telemetry.addData("Probable Color:", "Red");
            telemetry.update();
            jewelHitter.setPosition(0);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (sensorColor.blue() > sensorColor.red() && sensorColor.blue() > sensorColor.green()) {
            telemetry.addData("Probable Color:", "Blue");
            telemetry.update();
            jewelHitter.setPosition(1);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (sensorColor.green() > sensorColor.blue() && sensorColor.green() > sensorColor.red()) {
            telemetry.addData("Probable Color:", "Green");
            telemetry.addData("You Suck", "Bruv");
            telemetry.update();
        }


        // change the background color to match the color detected by the RGB sensor.
        // pass a reference to the hue, saturation, and value array as an argument
        // to the HSVToColor method.
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
            }
        });

        telemetry.update();
        armServo.setPosition(0);
        jewelHitter.setPosition(.5);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        // Set the panel back to the default color
        /*boolean post = relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(Color.WHITE);
            }
        });
        */
    }
}
