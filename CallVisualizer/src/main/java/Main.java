import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.audio.AudioReceiveHandler;
import net.dv8tion.jda.api.audio.CombinedAudio;
import net.dv8tion.jda.api.audio.SpeakingMode;
import net.dv8tion.jda.api.audio.hooks.ConnectionListener;
import net.dv8tion.jda.api.audio.hooks.ConnectionStatus;
import net.dv8tion.jda.api.audio.hooks.ListenerProxy;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.GenericEvent;
import net.dv8tion.jda.api.events.channel.voice.update.GenericVoiceChannelUpdateEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.IEventManager;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.annotation.Nonnull;
import javax.security.auth.login.LoginException;
import javax.sound.sampled.AudioInputStream;
import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class Main extends ListenerAdapter {
    private String newline = System.lineSeparator();
    private char quotationMark = '"';
    private static JDA shard;
    private JFrame frame;
    private ArrayList<VoiceUser> userArrayList;
    public static void main(String[] args) throws LoginException, IOException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "NjY2MzE3Nzc4MDM4NDIzNTY0.XiYxZg.rdGHxw1mN_hjWx_ZorEnrJ-5TAk";
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
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*@Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent event) {
        super.onGuildVoiceJoin(event);
        userArrayList.add(new VoiceUser(event.getEntity().getNickname(), event.getMember().getColor(), false));
        System.out.println(event.getMember().getUser().getName() + " joined " + event.getChannelJoined());
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent event) {
        super.onGuildVoiceLeave(event);
        userArrayList.clear();
        for(Member member : event.getGuild().getMembers()){
            userArrayList.add(new VoiceUser(member.getNickname(), member.getColor(), false));
        }

        System.out.println(event.getMember().getUser().getName() + " left " + event.getChannelLeft());
    }*/

    public void react(@Nonnull MessageReceivedEvent event, String input) throws IOException, InterruptedException {
        char prefix = getTextFromFile("prefix").charAt(0);
        if (input.startsWith("" + prefix)) {
            //Ping Pong
            if (input.equalsIgnoreCase(prefix + "ping")) {
                say(event, "pong!");
            }
            //Set prefix
            if (input.startsWith(prefix + "prefix")) {
                char newPrefix = input.charAt(input.indexOf('x') + 2);
                if (prefix != newPrefix) {
                    overwrite("prefix", "prefix:" + newline + newPrefix);
                    say(event, ":white_check_mark: Prefix set to " + quotationMark + getTextFromFile("prefix").charAt(0) + quotationMark + "!");
                } else {
                    say(event, ":x: " + quotationMark + prefix + quotationMark + " was already the prefix!");
                }
            }
            //MAIN
            if (input.startsWith(prefix + "visualize")) {
                String channelName = input.substring(11);
                System.out.println(channelName);
                VoiceChannel channel = shard.getVoiceChannelsByName(channelName, true).get(0);

                event.getGuild().getAudioManager().openAudioConnection(channel);

                channel.getGuild().getAudioManager().setReceivingHandler(
                        new AudioReceiveHandler() {
                            @Override
                            public void handleCombinedAudio(@Nonnull CombinedAudio combinedAudio) {
                                //has all audio and list of all users speaking
                                userArrayList.clear();
                                for(Member member : channel.getGuild().getMembers()){
                                    userArrayList.add(new VoiceUser(member.getNickname(), member.getColor(), combinedAudio.getUsers().contains(member.getUser())));
                                }
                                System.out.println(userArrayList.toString());
                            }
                        });
                for(int i=0; i<100; i++){
                    if(userArrayList != null) {
                        System.out.println(userArrayList.toString());
                    }
                    Thread.sleep(1000);
                }
            }
        }
    }

    public void visualize() {
        //repaint
    }

    public void updateGraphic() {
        frame.repaint();
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
        while ((n = reader.readLine()) != null) {
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
        while ((n = reader.readLine()) != null) {
            output = currentLine == line ? n : "";
            if (!output.equals("")) break;
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

    public int rand(int x, int y) {
        int rand;
        int min = Math.min(x, y);
        int max = Math.max(x, y) + 1;
        max -= Math.min(min, 0);
        min = Math.max(min, 0);
        rand = (int) (Math.random() * (max - min)) + min;
        rand += Math.min(0, Math.min(x, y));
        return rand;
    }

    public MessageEmbed blockMessage(String title, String pfp, String description, Color color) {
        if (pfp == null) {
            pfp = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRsFR9wycQvfZscubeEN9zu7T5DcDiKGLQrt17Zg3kdI8SWgJ_f&s";
        }
        EmbedBuilder eb = new EmbedBuilder();
        eb.setDescription(description);
        eb.setAuthor(title, null, pfp);
        eb.setColor(color);
        return eb.build();
    }

    public User mentionToUserID(String mention) {
        mention = mention.replace("<", "");
        mention = mention.replace(">", "");
        mention = mention.replace("@", "");
        return getUserById(shard, mention);
    }

    public User getUserById(JDA shard, String userId) {
        User user = shard.getUserById(userId);
        if (user == null) {
            user = shard.retrieveUserById(userId).complete();
        }
        return user;
    }
}
