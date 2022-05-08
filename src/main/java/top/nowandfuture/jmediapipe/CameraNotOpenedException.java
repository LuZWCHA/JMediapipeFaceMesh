package top.nowandfuture.jmediapipe.exception;

public class CameraNotOpenedException extends Exception{
    public CameraNotOpenedException(String message){
        super(message);
    }

    public CameraNotOpenedException(){
        super();
    }
}
