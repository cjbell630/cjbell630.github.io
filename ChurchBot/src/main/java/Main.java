import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.JDA;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;

import java.awt.*;
import java.io.*;

import java.util.ArrayList;
import java.util.List;

public class Main extends ListenerAdapter {
    private String newline = System.lineSeparator();
    private char quotationMark = '"';
    private static JDA shard;

    public static void main(String[] args) throws LoginException, IOException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "NjQzNDkzMjEzNjgzODQzMDcz.Xdygmw.Ro0yTUQb5UYrutGVS_g1Wnu0wfE";
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
            //Call+Response

            if (input.equalsIgnoreCase(prefix + "ping")) {
                say(event, "pong!");
            }
            if (input.equalsIgnoreCase(prefix + "hello")) {
                say(event,"hello i am churc");
            }
            //Setter

            //Quote specific user
            if (input.startsWith(prefix + "quote") && input.length()>7) {
                //get user <@#>
                String user = input.substring(input.indexOf('e') + 2);
                String quotedMessage;
                //save shortened msgs to list
                List<Message> history = event.getChannel().getHistory().retrievePast(100).complete();
                //offset by one if the user is quoting themselves
                int offset = authorToMention(event.getAuthor()).equals(user)? 1: 0;
                int count = offset;
                //while not at end of list and username is wrong
                while (count < history.size()-offset && !authorToMention(history.get(count).getAuthor()).equals(user)) {
                    count++;
                }
                if(count==history.size()-offset) count--;
                if(authorToMention(history.get(count).getAuthor()).equals(user)) {
                    //get RAW content of message, maybe try something else if it doesn't work
                    quotedMessage = history.get(count).getContentRaw();
                    write(user + newline + quotedMessage, "quotes.txt");
                    say(event, ":white_check_mark: Quoted user " + user + " as saying " + quotationMark + quotedMessage + quotationMark + "!");
                }else {
                    say(event, ":x: Could not find recent message from user " + user + ".");
                }
            }
            //Quote most recent msg
            if (input.startsWith(prefix + "quote") &&  input.length()<=7) {
                List<Message> history = event.getChannel().getHistory().retrievePast(2).complete();
                if(history.size() == 2){
                    String quotedMessage = history.get(1).getContentRaw();
                    String user = authorToMention(history.get(1).getAuthor());
                    write(user + newline + quotedMessage, "quotes.txt");
                    say(event, ":white_check_mark: Quoted user " + user + " as saying " + quotationMark + quotedMessage + quotationMark + "!");
                }else {
                    say(event, ":x: Could not get recent messages.");
                }
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
            //Set admin
            if (input.startsWith(prefix + "admin")) {
                String user = input.substring(input.indexOf('n') + 2);
                String adminsFileText = getTextFromFile("admins.txt");
                //make sure the person trying to do that is an admin
                if (adminsFileText.contains(authorToMention(event.getAuthor()))) {
                    //Make sure it's a user
                    if (user.contains("@") && user.contains("<") && user.contains(">")) {
                        //make sure the requested user isn't already an admin
                        if (adminsFileText.contains(user)) {
                            say(event, ":x: User " + user + " is already an admin!");
                        } else {
                            write(user, "admins.txt");
                            say(event, ":white_check_mark: User " + user + " is now an admin!");
                        }
                    } else {
                        say(event, ":x: " + user + ", You don't have that permission!");
                    }
                } else {
                    say(event,":x: " + user + ", You don't have that permission!");
                }
            }


            //Getter

            //vote for song
            if (input.startsWith(prefix + "vote")){
                List<Message> history = event.getChannel().getHistory().retrievePast(100).complete();
                if(history.contains("")) {
                    if (getTextFromFile("voters.txt").contains(event.getAuthor().getId())) {
                        String vote = input.substring(input.indexOf('e') + 2);
                        int value = (vote == ":thumbsup:" ? 1 : vote == ":thumbsdown:" ? -1 : 0);
                        int oldValue = Integer.parseInt(getLineFromFile("votes.txt", 0));
                        overwrite("votes.txt", "" + (value + oldValue));
                    }
                }
            }
            //get random quote from specific user
            if (input.startsWith(prefix + "rq") && input.length()>5) {
                ArrayList<String> quotes = new ArrayList<>();
                String user = input.substring(input.indexOf('q') + 2);
                int lines = nlInFile("quotes.txt");
                for(int i=0; i<lines; i++){
                    if(getLineFromFile("quotes.txt", 2*i).equals(user)) {
                        quotes.add(getLineFromFile("quotes.txt", 2*i+1));
                    }
                }
                if(quotes.size()>0) {
                    int rnd = rand(0, quotes.size() - 1);
                    say(event, blockMessage(mentionToUserID(user).getName(), mentionToUserID(user).getAvatarUrl(), quotes.get(rnd), Color.green));
                }else{
                    say(event, ":x: Could not find quotes from user " + user + ".");
                }

            }
            //Get random quote from entire db
            if(input.startsWith(prefix + "rq" ) && input.length()<=5){
                ArrayList<String> quotes = new ArrayList<>();
                int lines = nlInFile("quotes.txt");
                for(int i=0; i<lines; i++) {
                    quotes.add(getLineFromFile("quotes.txt", i));
                }
                if(quotes.size()>0) {
                    int rnd = (rand(0, quotes.size()-2)*2)%(quotes.size());
                    say(event, blockMessage(mentionToUserID(quotes.get(rnd)).getName(), mentionToUserID(quotes.get(rnd)).getAvatarUrl(), quotes.get(rnd+1), Color.green));
                }else{
                    say(event, ":x: Could not find quotes.");
                }
            }
        }
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
    public void saveMessage(net.dv8tion.jda.api.entities.User user, String msg) throws IOException {
        String name = String.valueOf(user).substring(user.getName().length() + 3, String.valueOf(user).length() - 1);
        msg = "start_file" + newline + msg.substring(user.getName().length()+8, msg.length()-(name.length()+2));
        if(!getTextFromFile("users.txt").contains(name)){
            write("", name + ".txt");
        }
        overwrite(name + ".txt", msg);
    }
    public String lastMessage(String user) throws IOException {
        String name = user.substring(2, user.length()-1);
        return getTextFromFile(name + ".txt");
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
    public int nlInFile(String fileName) throws IOException {
        String file = getTextFromFile(fileName);
        int lines = 0;
        while(!getLineFromFile(fileName, lines).equals("")){
            lines++;
        }
        return lines;
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
