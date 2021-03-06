package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;

import java.util.ArrayList;
import java.util.List;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

@TeleOp(name="Ian Vuforia code", group ="Concept")
public class vuforiaTeleOp extends LinearOpMode {
    vuforiaHardware robot = new vuforiaHardware();
    ArmHardware Robot = new ArmHardware();

    @Override
    public void runOpMode() {

        robot.init(hardwareMap);
        robot.initVuforia();



        List<VuforiaTrackable> allTrackables = new ArrayList<VuforiaTrackable>();
        allTrackables.addAll(robot.targetsSkyStone);

        // I have this comment out because of the reason so we don't have to wait to play.
        waitForStart();

        while (opModeIsActive()) {


            robot.targetsSkyStone.activate();
            while (!isStopRequested()) {

                // check all the trackable targets to see which one (if any) is visible.
                robot.targetVisible = false;
                for (VuforiaTrackable trackable : allTrackables) {

                    if (((VuforiaTrackableDefaultListener) trackable.getListener()).isVisible()) {
                        if (trackable.getName().equals("Front Perimeter 1")) {
                            telemetry.addData("red depot", trackable.getName());
                        } else if (trackable.getName().equals("Front Perimeter 2")) {
                            telemetry.addData("blue depot", trackable.getName());
                        }
                        else if (trackable.getName().equals("Red Perimeter 1")) {
                            telemetry.addData("red alliance wall", trackable.getName());
                        }
                        else if (trackable.getName().equals("Red Perimeter 2")) {
                            telemetry.addData("red alliance wall", trackable.getName());
                        }

                        else if (trackable.getName().equals("Blue Perimeter 1")) {
                            telemetry.addData(" blue alliance wall", trackable.getName());
                        }
                        else if (trackable.getName().equals("Blue Perimeter 2")) {
                            telemetry.addData("blue alliance wall ", trackable.getName());
                        }


                        else if (trackable.getName().equals("Rear Perimeter 2")) {
                            telemetry.addData("build side/ red side", trackable.getName());
                        } else if (trackable.getName().equals("Rear Perimeter 1")) {
                            telemetry.addData("build side / blue side", trackable.getName());
                        }
                        else {
                            telemetry.addLine("Sky Stones");
                        }
                        if(trackable.getName().equals("Sky Stones")){
                            //move the arm
                        }


                        robot.targetVisible = true;

                        // getUpdatedRobotLocation() will return null if no new information is available since
                        // the last time that call was made, or if the trackable is not currently visible.
                        OpenGLMatrix robotLocationTransform = ((VuforiaTrackableDefaultListener) trackable.getListener()).getUpdatedRobotLocation();
                        if (robotLocationTransform != null) {
                            robot.lastLocation = robotLocationTransform;
                        }

                        break;
                    }
                }

                // Provide feedback as to where the robot is located (if we know).
                if (robot.targetVisible) {
                    // express position (translation) of robot in inches.
                    VectorF translation = robot.lastLocation.getTranslation();
                    telemetry.addData("Pos (in)", "{X, Y, Z} = %.1f, %.1f, %.1f",
                            translation.get(0) / robot.mmPerInch, translation.get(1) / robot.mmPerInch, translation.get(2) / robot.mmPerInch);

                    // express the rotation of the robot in degrees.
                    Orientation rotation = Orientation.getOrientation(robot.lastLocation, EXTRINSIC, XYZ, DEGREES);
                    telemetry.addData("Rot (deg)", "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f", rotation.firstAngle, rotation.secondAngle, rotation.thirdAngle);

                } else {
                    telemetry.addData("Visible Target", "none");
                }
                telemetry.update();


            }
            // Disable Tracking when we are done;
            robot.targetsSkyStone.deactivate();
        }

    }

    public void scanningForLocation() {
        while(opModeIsActive() && !robot.locationFlag) {
            robot.scanTheRoom();
        }
    }
}
