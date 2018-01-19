package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import android.util.Range;


/**
 * Created by Hirsh Ramani on 1/16/2018.
 */
@Autonomous(name = "JewelArmBlue", group = "JewelArm")
public class JewelArmBlue extends LinearOpMode {
    ColorSensor sensorRGB;
    Servo armServo;
    Servo jewelHitter;
    boolean currentState = false;

    public void runOpMode() {
        waitForStart();
        sensorRGB = hardwareMap.colorSensor.get("communismcolor");
        armServo = hardwareMap.servo.get("armServo");
        jewelHitter = hardwareMap.servo.get("jewelHitter");
        //bring down arm
        jewelHitter.setPosition(.5);
        armServo.setPosition(.5);
        while (currentState == false) {
            if (sensorRGB.red() > sensorRGB.blue() && armServo.getPosition() == .5) {
                //turn right
                telemetry.addData("Color =", "Red");
                telemetry.update();
                jewelHitter.setPosition(0);
                currentState = true;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (sensorRGB.red() < sensorRGB.blue() && armServo.getPosition() == .5) {
                //turn left
                telemetry.addData("Color =", "Blue");
                telemetry.update();
                jewelHitter.setPosition(1);
                currentState = true;
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                telemetry.addData("currentState", "failure");
                telemetry.update();
            }
        }
        try {
            Thread.sleep(30690);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        armServo.setPosition(0);
            jewelHitter.setPosition(.5);

    }
}


