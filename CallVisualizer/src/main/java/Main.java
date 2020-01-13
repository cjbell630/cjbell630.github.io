import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.JDA;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

import java.awt.*;
import java.io.*;

public class Main extends ListenerAdapter {
    private String newline = System.lineSeparator();
    private char quotationMark = '"';
    private static JDA shard;

    public static void main(String[] args) throws LoginException, IOException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "NjY2MzE3Nzc4MDM4NDIzNTY0.Xhya0w.wYl2usRl-PfgH-bK_R6_cwrt2FA";
        builder.setToken(token);
        builder.addEventListeners(new Main());
        shard = builder.build();
    }

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {

        if (event.getAuthor().isBot()) {
            return;
        }
        System.out.println("Received message from " + event.getAuthor().getName() + ": " + event.getMessage().getContentDisplay());
        try {
            react(event, event.getMessage().getContentRaw());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void react(@Nonnull MessageReceivedEvent event, String input) throws IOException {
        char prefix = getTextFromFile("prefix").charAt(0);
        if (input.startsWith("" + prefix)) {
            //Ping Pong
            if (input.equalsIgnoreCase(prefix + "ping")) {
                say(event, "pong!");
            }
            //Set prefix
            if(input.startsWith(prefix + "prefix")) {
                char newPrefix = input.charAt(input.indexOf('x') + 2);
                if(prefix!=newPrefix){
                    overwrite("prefix", "prefix:" + newline + newPrefix);
                    say(event, ":white_check_mark: Prefix set to " + quotationMark + getTextFromFile("prefix").charAt(0) + quotationMark + "!");
                }else{
                    say(event, ":x: " + quotationMark + prefix + quotationMark + " was already the prefix!");
                }
            }
            //MAIN
            if(input.startsWith(prefix + "go")){
                public void visualize();
            }
        }
    }

    public void visualize(){
        //repaint
    }
    public void write(String s, String fileName) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
        writer.append(s + newline);
        writer.close();
    }
    public void overwrite(String fileName, String s) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
        writer.write(s + newline);
        writer.close();
    }

    public String getTextFromFile(String fileName) throws IOException {
        String output = "";
        String n = "";
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        while ((n=reader.readLine()) != null) {
            output += n + newline;
        }
        reader.close();
        return output;
    }

    public String getLineFromFile(String fileName, int line) throws IOException {
        String output = "";
        String n;
        int currentLine = 0;
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        while ((n=reader.readLine()) != null) {
            output = currentLine == line ? n : "";
            if(!output.equals("")) break;
            currentLine++;
        }
        reader.close();
        return output;

    }

    public String authorToMention(net.dv8tion.jda.api.entities.User author) {
        return "<@" + String.valueOf(author).substring(author.getName().length() + 3, String.valueOf(author).length() - 1) + ">";
    }

    public void say(MessageReceivedEvent event, String msg) {
        event.getChannel().sendMessage(msg).queue();
    }
    public void say(MessageReceivedEvent event, MessageEmbed msg) {
        event.getChannel().sendMessage(msg).queue();
    }

    public int rand(int x, int y){
        int rand;
        int min = Math.min(x, y);
        int max = Math.max(x, y)+1;
        max -= Math.min(min, 0);
        min = Math.max(min, 0);
        rand = (int)(Math.random() * (max-min)) + min;
        rand += Math.min(0, Math.min(x,y));
        return rand;
    }

    public MessageEmbed blockMessage(String title, String pfp, String description, Color color){
        if(pfp==null){
            pfp = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsFR9wycQvfZscubeEN9zu7T5DcDiKGLQrt17Zg3kdI8SWgJ_f&s";
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription(description);
        eb.setAuthor(title, null, pfp);
        eb.setColor(color);
        return eb.build();
    }
    public User mentionToUserID(String mention){
        mention = mention.replace("<", "");
        mention = mention.replace(">", "");
        mention = mention.replace("@", "");
        return getUserById(shard, mention);
    }
    public User getUserById(JDA shard, String userId){
        User user = shard.getUserById(userId);
        if (user == null) {
            user = shard.retrieveUserById(userId).complete();
        }
        return user;
    }
}
