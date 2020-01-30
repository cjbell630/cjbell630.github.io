import java.awt.*;

public class VoiceUser {
    private String name;
    private Color color;
    private boolean status;

    public VoiceUser(String userName, Color roleColor, boolean voiceStatus){
        name = userName;
        color = roleColor;
        status = voiceStatus;
    }

    public void setName(String userName){
        name = userName;
    }

    public String getName(){
        return name;
    }

    public void setColor(Color roleColor){
        color = roleColor;
    }

    public Color getColor(){
        return color;
    }

    public void setStatus(boolean voiceStatus){
        status = voiceStatus;
    }

    public boolean getStatus(){
        return status;
    }

    public String toString(){
        return "Name: " + name + '\n' + "Color: " + color + '\n' + "Status: " + status;
    }
}
