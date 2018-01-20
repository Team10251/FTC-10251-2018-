package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import android.app.Activity;
import android.graphics.Color;
import android.util.Range;
import android.view.View;


/**
 * Created by Hirsh Ramani on 1/16/2018.
 */
@Disabled
@Autonomous(name = "JewelArmBlue", group = "JewelArm")
public class JewelArmBlue extends LinearOpMode {
    ColorSensor sensorRGB;
    Servo armServo;
    Servo jewelHitter;
    boolean currentState = false;
    float hsvValues[] = {0F,0F,0F};
    final float values[] = hsvValues;

    public void runOpMode() {
        waitForStart();
        sensorRGB = hardwareMap.colorSensor.get("communismcolor");
        armServo = hardwareMap.servo.get("armServo");
        jewelHitter = hardwareMap.servo.get("jewelHitter");
        //bring down arm
        jewelHitter.setPosition(.5);
        armServo.setPosition(.5);
        sensorRGB.enableLed(false);
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.RelativeLayout);
        while (currentState == false) {
            //telemetry.addData("Red",sensorRGB.red());
          // telemetry.addData("Blue",sensorRGB.blue());
          //  telemetry.addData("Green",sensorRGB.green());
            telemetry.addData("rgb", sensorRGB.argb());
            telemetry.update();
            Color.RGBToHSV(sensorRGB.red() * 8, sensorRGB.green() * 8, sensorRGB.blue() * 8, hsvValues);
            relativeLayout.post(new Runnable() {
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
                }
            });
            if (sensorRGB.red() > sensorRGB.blue() && armServo.getPosition() == .5) {
                //turn right
                //telemetry.addData("Red =", sensorRGB.red());
                //telemetry.update();
                jewelHitter.setPosition(0);
                /*try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            } else if (sensorRGB.red() < sensorRGB.blue() && armServo.getPosition() == .5) {
                //turn left
                //telemetry.addData("Blue =", sensorRGB.blue());
                //telemetry.update();
                jewelHitter.setPosition(1);
                /*try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
            } else {
                telemetry.addData("currentState", "failure");
                telemetry.update();
            }
        }
        telemetry.addLine("I am done bruv");
        telemetry.update();
        try {
            Thread.sleep(3069);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        armServo.setPosition(0);
            jewelHitter.setPosition(.5);

    }
}


