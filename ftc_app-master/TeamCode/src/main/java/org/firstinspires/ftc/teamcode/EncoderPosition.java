package org.firstinspires.ftc.teamcode;
import android.util.Log;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.exception.RobotCoreException;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.*;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import java.util.Locale;


// * Created by definitly not HIRSH as he would mess it up and it would explode on 8/18/2016.
@TeleOp(name= "Encoder Tester")
public class EncoderPosition extends OpMode {
    double[] rollAngle = new double[2], pitchAngle = new double[2], yawAngle = new double[2];
    Orientation angles;
    BNO055IMU imu;
    String angleDouble = "hi";
    public double gyroAngle;
    boolean speedMode = false;
    DcMotorEx leftMotor;
    DcMotorEx rightMotor;
    DcMotorEx middleMotor;
    DcMotorEx glyphMotor; //Pulley
    Servo servo;
    Servo servo3;
    int state = 0;
    boolean countUp = false;
    int countsinceapressed = 0;
    boolean countUp2 = false;
    int countsincebpressed = 0;
    //HDrive2 calculator;
    HDriveFCCalc calculator;
    Servo servo2;
    double armAngle = .5;
    double offset = 0;
    int encoder = 0;
    int shootTimer = 6;
    boolean bumperPressed = false;
    boolean bumperIsPressed = false;
    boolean hulianNotAnH = false;
    boolean shootTimerDone = false;
    Servo buttonPusher;
    boolean lezGoSlow = false;
    boolean state1;
    Servo IntakeServo;
    DcMotor IntakeMotor;
    public EncoderPosition(){

    }
    public void init(){

        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();
        imu = hardwareMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
        imu.startAccelerationIntegration(new Position(), new Velocity(), 1000);
        leftMotor = (DcMotorEx)hardwareMap.get(DcMotor.class,"leftMotor");
        rightMotor = (DcMotorEx)hardwareMap.get(DcMotor.class, "rightMotor");
        middleMotor = (DcMotorEx)hardwareMap.get(DcMotor.class, "middleMotor");
        glyphMotor = (DcMotorEx)hardwareMap.get(DcMotor.class, "Hirsh is very dumb");
        //shooter = hardwareMap.dcMotor.get("shooter");
        //servo3 = hardwareMap.servo.get("servo3");
        //arm = hardwareMap.dcMotor.get("arm");
        //servo2 = hardwareMap.servo.get("servo2");
        //buttonPusher = hardwareMap.servo.get("servo2");



        calculator = new HDriveFCCalc();
        //servo = hardwareMap.crservo.get("servo");
        rightMotor.setDirection(DcMotor.Direction.REVERSE);
        middleMotor.setDirection(DcMotor.Direction.REVERSE);
        glyphMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        middleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void loop(){
        int i = 0;
        angles   = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
        angleDouble = formatAngle(angles.angleUnit, angles.firstAngle);
        float leftX = gamepad1.left_stick_x;
        float leftY = gamepad1.left_stick_y;
        float rightX = gamepad1.right_stick_x;
        float rightY = gamepad1.right_stick_y;
        float left = gamepad1.left_trigger;
        float right = gamepad1.right_trigger;
        float leftTrigger = gamepad2.left_trigger;
        float rightTrigger = gamepad2.right_trigger;
        boolean buttonAPressed = gamepad1.a;
        boolean buttonXPressed = gamepad1.x;
        boolean buttonAPressed2 = gamepad2.a;
        boolean buttonXPressed2 = gamepad2.x;
        boolean dPadUp    = gamepad1.dpad_up;
        boolean dPadDown  = gamepad1.dpad_down;
        boolean dPadLeft  = gamepad1.dpad_left;
        boolean dPadRight = gamepad1.dpad_right;
        telemetry.addData("Middle",middleMotor.getCurrentPosition());
        telemetry.addData("Left",leftMotor.getCurrentPosition());
        telemetry.addData("Right",rightMotor.getCurrentPosition());
        telemetry.addData("Arm",glyphMotor.getCurrentPosition());

    }

    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }
    String formatDegrees(double degrees) {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));


    }
}


