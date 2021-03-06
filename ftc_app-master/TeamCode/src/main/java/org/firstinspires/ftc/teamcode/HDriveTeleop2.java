package org.firstinspires.ftc.teamcode;
import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.Velocity;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.Locale;
// * Created by definitly not HIRSH as he would mess it up and it would explode on 8/18/2016.
@TeleOp(name= "HDriveTeleop2")
public class HDriveTeleop2 extends OpMode {
    double[] rollAngle = new double[2], pitchAngle = new double[2], yawAngle = new double[2];
    float rightX;
    float leftX;
    float leftY;
    Orientation angles;
    BNO055IMU imu;
    String angleDouble = "hi";
    public double gyroAngle;
    boolean speedMode = false;
    DcMotorEx leftMotor;
    DcMotorEx rightMotor;
    DcMotorEx middleMotor;
    DcMotorEx middleMotorNumberoDdox;
    DcMotorEx glyphMotor; //Pulley
    DcMotorEx arm;
    int glyphMotorState;
    int glyphResetCount = 0;
    Servo servo;
    Servo servo3;
    Servo claw1;
    Servo claw2;
    boolean countUp = false;
    boolean timeState = true;
    boolean fieldCentric = true;
    int countsinceapressed = 0;
    boolean countUp2 = false;
    int countsincebpressed = 0;
    //HDrive2 calculator;
    HDriveFCCalc calculator;
    Servo servo2;
    double armAngle = .5;
    double offset = 0;
    double yAngle = 0;
    int encoder = 0;
    int shootTimer = 6;
    boolean encoderReset = false;
    int fieldCentricCounter = 0;
    boolean bumperPressed = false;
    boolean bumperIsPressed = false;
    boolean hulianNotAnH = false;
    boolean shootTimerDone = false;
    boolean yPressed = false;
    double stateGlyph = 0;
    double position = 0;
    double scale;
    double time = System.currentTimeMillis();
    Servo buttonPusher;
    int glyphUpDownCounter = 0;
    boolean lezGoSlow = false;
    boolean state1;
    boolean closedClaw = false;
    boolean runningclaw = false;
    boolean firstY = true;
    int glyphCounter = 0;
    int glyphLevel = 0;
    double lastLiftPosition = 0;
    double currentLiftPosition = 0;
    Servo IntakeServo;
    DcMotor IntakeMotor;
    public HDriveTeleop2(){
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
        middleMotorNumberoDdox = (DcMotorEx)hardwareMap.get(DcMotor.class, "middleMotor2");
        glyphMotor = (DcMotorEx)hardwareMap.get(DcMotor.class, "Hirsh is very dumb");
        claw1 = hardwareMap.servo.get("claw1");
        claw2 = hardwareMap.servo.get("claw2");
        arm = (DcMotorEx)hardwareMap.get(DcMotor.class, "arm");
        //shooter = hardwareMap.dcMotor.get("shooter");
        //servo3 = hardwareMap.servo.get("servo3");
        //arm = hardwareMap.dcMotor.get("arm");
        //servo2 = hardwareMap .servo.get("servo2");
        //buttonPusher = hardwareMap.servo.get("servo2");
        calculator = new HDriveFCCalc();
        //servo = hardwareMap.crservo.get("servo");
        leftMotor.setDirection(DcMotor.Direction.REVERSE);
        middleMotor.setDirection(DcMotor.Direction.REVERSE);
        middleMotorNumberoDdox.setDirection(DcMotor.Direction.REVERSE);
        glyphMotor.setDirection(DcMotor.Direction.REVERSE);
        glyphMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        PIDCoefficients pid = glyphMotor.getPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION);
        pid.d = 0;
        glyphMotor.setPIDCoefficients(DcMotor.RunMode.RUN_TO_POSITION, pid);
        //1= position; 0 = encoder; 2 = power
        glyphMotorState = 1;
        arm.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //shooter.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }
    public void loop(){

        lastLiftPosition = currentLiftPosition;
        currentLiftPosition = glyphMotor.getCurrentPosition();
        if(glyphResetCount < 100){
            glyphResetCount++;
        }
        if(glyphCounter < 10){
            glyphCounter++;
        }
        if(glyphUpDownCounter < 10){
            glyphUpDownCounter++;
        }
        if(fieldCentricCounter < 10) {
            fieldCentricCounter++;
        }
        int i = 0;
        angles   = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.ZYX);
        angleDouble = formatAngle(angles.angleUnit, angles.firstAngle);
        if(yPressed == false) {
            leftX = gamepad1.left_stick_x;
            leftY = gamepad1.left_stick_y;
        }
        if(gamepad1.a != true && yPressed == false) {
            rightX = gamepad1.right_stick_x;
        }
        float rightY = gamepad1.right_stick_y;
        float left = gamepad1.left_trigger;
        float right = gamepad1.right_trigger;
        float leftTrigger = gamepad2.left_trigger;
        float rightTrigger = gamepad2.right_trigger;
        boolean buttonAPressed = false;
        boolean buttonXPressed = gamepad1.x;
        boolean buttonAPressed2 = gamepad2.a;
        boolean buttonXPressed2 = gamepad2.x;
        boolean dPadUp    = gamepad1.dpad_up;
        boolean dPadDown  = gamepad1.dpad_down;
        boolean dPadLeft  = gamepad1.dpad_left;
        boolean dPadRight = gamepad1.dpad_right;
        if(gamepad1.left_stick_x > 0 || gamepad1.left_stick_x < 0 || gamepad1.left_stick_y > 0 || gamepad1.left_stick_y < 0 || gamepad1.right_stick_x > 0 || gamepad1.right_stick_x < 0 && timeState == true) {
            timeState = false;
        }
        else if(gamepad1.left_stick_x == 0 && gamepad1.left_stick_x == 0 && gamepad1.left_stick_y == 0 && gamepad1.left_stick_y == 0 && gamepad1.right_stick_x == 0 && gamepad1.right_stick_x == 0){
            timeState = true;
            time = System.currentTimeMillis();
        }
        //bumperPressed = gamepad1.right_bumpter;
        //telemetry.addData("Gyro", angleDouble);
        //telemetry.update();
        if(gamepad1.right_bumper){
            claw1.setPosition(.2);
            claw2.setPosition(.8);
            stateGlyph = 1;
        }
        if(countUp){
            if(countsinceapressed < 10){
                countsinceapressed++;
            }
            else{
                countUp = false;
                countsinceapressed = 0;
            }
        }
        if(buttonAPressed&& !countUp) {
            countUp = true;
            if (speedMode == false) {
                speedMode = true;
                leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                middleMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                middleMotorNumberoDdox.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                telemetry.addData("Mode", "Speed");
                telemetry.update();
            } else if (speedMode == true) {
                speedMode = false;
                leftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                rightMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                middleMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                middleMotorNumberoDdox.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                telemetry.addData("Mode", "Power");
                telemetry.update();
            }
        }
        // telemetry.addData("Encoder Position", shooter.getCurrentPosition());
        // telemetry.addData("Angle", Double.parseDouble(angleDouble)+offset);
        // telemetry.addData("Left Trigger", gamepad1.left_trigger);
        // telemetry.addData("Left Stick X" , gamepad1.left_stick_x);
        //telemetry.addData("Right Stick Y" , gamepad1.right_stick_x);
        //telemetry.addData("Left Stick X" , gamepad1.left_stick_y);
        //telemetry.addData("Right Stick Y" , gamepad1.right_stick_y);
        //telemetry.update();
        /*if(gamepad1.y) {
            if(firstY) {
                firstY = false;
                yAngle = Double.parseDouble(angleDouble) + offset;
            }
            if((Double.parseDouble(angleDouble) + offset) < (yAngle + 180)) {
                rightX = 1;
            }

            else{
                rightX = 0;
            }
        }*/
        else {
            firstY = true;
        }
        if(gamepad1.b == true){
            offset = Double.parseDouble(angleDouble);
            offset = -offset;
        }
        if(gamepad1.a == true) {
            lezGoSlow = true;
        }
        else {
            lezGoSlow = false;
        }
        if(gamepad1.x && fieldCentricCounter > 9) {

            fieldCentric = !fieldCentric;

            fieldCentricCounter = 0;
        }
        scale = ((System.currentTimeMillis()-time)/500);
        if(fieldCentric) {
            calculator.calculateMovement(leftX, leftY, rightX, Double.parseDouble(angleDouble) + offset);
        }
        else {
            calculator.calculateMovement(leftX,leftY,rightX, 0);
        }
        if(lezGoSlow) {
            if (!speedMode) {
                leftMotor.setPower(.3 * calculator.getLeftDrive());
                rightMotor.setPower(.3 * calculator.getRightDrive());
                middleMotor.setPower(.3*-calculator.getMiddleDrive());
                middleMotorNumberoDdox.setPower(.6*-calculator.getMiddleDrive());
            } else {
                leftMotor.setPower(.3*calculator.getLeftDrive());
                rightMotor.setPower(.3*calculator.getRightDrive());
                middleMotor.setPower(.3*-calculator.getMiddleDrive());
                middleMotorNumberoDdox.setPower(.6*-calculator.getMiddleDrive());
            }
        }
        else {
            if (!speedMode) {
                leftMotor.setPower(scale * calculator.getLeftDrive());
                rightMotor.setPower(scale * calculator.getRightDrive());
                middleMotor.setPower(scale*-calculator.getMiddleDrive());
                middleMotorNumberoDdox.setPower(scale*-calculator.getMiddleDrive());
            } else {
                leftMotor.setPower(scale * calculator.getLeftDrive());
                rightMotor.setPower(scale * calculator.getRightDrive());
                middleMotor.setPower(scale*-calculator.getMiddleDrive());
                middleMotorNumberoDdox.setPower(scale*-calculator.getMiddleDrive());
            }
        }
  /*   if(gamepad1.left_bumper == true) {
         leftMotor.setPower(-.1);
         rightMotor.setPower(-.1);
     }
     if(gamepad1.right_bumper == true) {
         leftMotor.setPower(.1);
         rightMotor.setPower(.1);
<<<<<<< HEAD
     }*/
        telemetry.addData("left Motor", leftMotor.getCurrentPosition());
        telemetry.addData("right Motor", rightMotor.getCurrentPosition());
        telemetry.addData("arm", arm.getCurrentPosition());
        telemetry.addData("Angle", Double.parseDouble(angleDouble) + offset);
        telemetry.addData("Angle 2", yAngle);
        telemetry.addData("Puley", glyphMotor.getCurrentPosition());
        telemetry.addData("time", System.currentTimeMillis());
        telemetry.addData("Start Time," , time);
        telemetry.addData("time state", timeState);
        telemetry.update();
        if(gamepad1.left_bumper == true && glyphUpDownCounter == 10) {
            glyphUpDownCounter = 0;
            if(glyphLevel < 2){
                glyphLevel++;
            }
            if(glyphLevel == 0){
                position = 0;
            }
            if(glyphLevel == 1){
                position = 700;
            }
            if(glyphLevel == 2){
                position = 1370;
            }
            glyphMotor.setPower(.35);
        }
        else if(gamepad1.left_trigger == 1 && glyphUpDownCounter == 10) {
            glyphUpDownCounter = 0;
            if(glyphLevel > 0){
                glyphLevel--;
            }
            if(glyphLevel == 0){
                position = 0;
            }
            if(glyphLevel == 1){
                position = 700;
            }
            if(glyphLevel == 2){
                position = 1370;
            }
            glyphMotor.setPower(-.35);
        }
        else if(gamepad1.dpad_up) {
            position = position + 25;
            glyphMotor.setPower(.35);
        }
        else if(gamepad1.dpad_down) {
            position = position - 25;
            glyphMotor.setPower(-.2);
        }
        if(position < 0){
            position = 0;
        }
        if(position > 1370){
            position = 1370;
        }
        if(gamepad1.back){
            encoderReset = true;
        }
        if(gamepad1.y) {
            yPressed = true;
            leftX = (float).5;
            rightX = (float)-.275;
        }
        else {
            yPressed = false;
        }
        if(!encoderReset) {
            if (glyphMotorState != 1) {
                glyphMotorState = 1;
                glyphMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            }
            glyphMotor.setTargetPosition((int) position);
        }
        else{
            if(glyphMotorState != 2){
                glyphMotorState = 2;
                glyphMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            }
            if(Math.abs(currentLiftPosition-lastLiftPosition) < 10&& glyphResetCount == 100){
                glyphMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                glyphResetCount = 0;
                encoderReset = false;
            }
        }
        if(stateGlyph == 0 && gamepad1.right_trigger == 1) {
            if(glyphCounter >= 10) {
                stateGlyph = 1;
                claw1.setPosition(.6);
                claw2.setPosition(.5);
                glyphCounter = 0;
            }
        }
        if(stateGlyph == 1 && gamepad1.right_trigger == 1) {
            if (glyphCounter >= 10) {
                claw1.setPosition(.8);
                claw2.setPosition(.35);
                stateGlyph = 0;
                glyphCounter = 0;
            }
        }
        //****************************************************************
       /*IntakeServo= hardwareMap.servo.get("IntakeServo");
       if (gamepad2.right_bumper == true){
           //IntakeServo.setPosition(1);
       }
       else {//IntakeServo.setPosition(0);
       }
      IntakeMotor= hardwareMap.dcMotor.get("IntakeMotor");
       if (gamepad2.left_bumper == true){
           IntakeMotor.setPower(1);
       }
       else {IntakeMotor.setPower(0);
       }
*/
        //.addData("Gyro",Double.parseDouble(angleDouble) + offset);
        //telemetry.update();
        //***********************************************************************
    }
    String formatAngle(AngleUnit angleUnit, double angle) {
        return formatDegrees(AngleUnit.DEGREES.fromUnit(angleUnit, angle));
    }
    String formatDegrees(double degrees) {
        return String.format(Locale.getDefault(), "%.1f", AngleUnit.DEGREES.normalize(degrees));
    }
}
