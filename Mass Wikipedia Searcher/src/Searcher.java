
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;
import javax.swing.text.html.parser.ParserDelegator;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

public class Searcher {
    private ArrayList<String> terms = new ArrayList<>();
    private final String baseURL = "https://en.wikipedia.org/w/index.php?search=";
    private final String searchTerms = "Anti-Federalists-\n" +
            "Dealignment-\n" +
            "Democratic-Republican Party-\n" +
            "Divided government-\n" +
            "Era of Good Feelings-\n" +
            "Federalist Party-\n" +
            "Grass-roots-\n" +
            "Gridlock-\n" +
            "Laissez-faire-\n" +
            "Linkage institutions-\n" +
            "McGovern-Fraser Commission-\n" +
            "Minor/third parties-\n" +
            "Winner-take electoral system-\n" +
            "Political efficacy-\n" +
            "Whig Party-\n" +
            "Populist Party-\n" +
            "Universal manhood suffrage-\n" +
            "Proportional representation-\n" +
            "Ticket splitting-\n" +
            "Realignment-\n" +
            "Superdelegates-\n" +
            "Republican Party-\n" +
            "Straight ticket-\n" +
            "Roosevelt Coalition-\n" +
            "Caucus-\n" +
            "Coalition-\n" +
            "Democratic National Committee-\n" +
            "Democratic Senatorial Campaign Committee-\n" +
            "Free Soil Party-\n" +
            "H. Ross Perot-\n" +
            "Ideological party-\n" +
            "Republican National Committee-\n" +
            "National Republican Congressional Committee-\n" +
            "Party convention-\n" +
            "Party platform-\n" +
            "Reform Party-\n" +
            "Single member district-\n" +
            "United We Stand America-\n" +
            "527’s-\n" +
            "Bipartisan Campaign Reform Act of 2002-\n" +
            "Blanket primary-\n" +
            "Buckley v. Valeo-\n" +
            "Bundling-\n" +
            "Campaign Reform Act of 1974-\n" +
            "Closed primaries-\n" +
            "Coattail effect-\n" +
            "Federal Election Commission-\n" +
            "Frontloading-\n" +
            "General election-\n" +
            "Help America Vote Act of 2002-\n" +
            "Mid-term election-\n" +
            "Open primaries-\n" +
            "PACs-\n" +
            "Plurality-\n" +
            "Soft money-\n" +
            "Swing states-\n" +
            "Washington State Grange v. Washington State Republican Party of 2008-\n" +
            "Citizens United v. FEC of 2010-\n" +
            "Electoral College electors-\n" +
            "Hard money-\n" +
            "Iowa caucuses-\n" +
            "Mandate-\n" +
            "Political machine-\n" +
            "Primary election-\n" +
            "Super Tuesday-\n" +
            "23rd amendment-\n" +
            "War chest-\n" +
            "McConnell v. FEC of 2003\n" +
            "Federal Elections Campaign Act-\n" +
            "Class action lawsuits-\n" +
            "Emily’s List-\n" +
            "Foundation grants-\n" +
            "Interest group-\n" +
            "Lobbying-\n" +
            "Public interest groups-\n" +
            "Union shop-\n" +
            "Honest Leadership and Open Government Act of 2007-\n" +
            "Lobbying Disclosure Act of 1995- \n" +
            "“revolving door’ as relates to interest groups-\n" +
            "Free rider-\n" +
            "Intergovernmental lobby-\n" +
            "K Street-\n" +
            "Purposive incentives-\n" +
            "Solidary incentives-\n" +
            "Think tanks-Wagner Act-\n" +
            "United States v. Harriss-\n" +
            "Sixteenth Amendment and how it relates to sections of tax code and campaign money such as sections501 ©4 and 3\n" +
            "Agenda setting-\n" +
            "Confidentiality of sources-\n" +
            "qual time rule-\n" +
            "Fairness doctrine-\n" +
            "“fourth branch”-\n" +
            "Press secretary-\n" +
            "Prior restraint-\n" +
            "Right of reply-\n" +
            "Sound bites-\n" +
            "Spin doctor-\n" +
            "Telecommunications Act of 1996-\n" +
            "Importance of You Tube in politics-\n" +
            "Bully pulpit-\n" +
            "Federal Communications Commission-\n" +
            "Gatekeeper role-\n" +
            "Horse race journalism-\n" +
            "Investigative reporting-\n" +
            "Libel-\n" +
            "Muckrakers-\n" +
            "NPR-\n" +
            "New York Times v. United States of 1971-\n" +
            "Off the record-\n" +
            "On the record-\n" +
            "Pentagon Papers-\n" +
            "PBS-\n" +
            "Scorekeeper role-\n" +
            "Shield laws-\n" +
            "Slander-\n" +
            "Talk radio-\n" +
            "Watchdog role-\n" +
            "Yellow journalism-\n";

    public void searchAndDisplay() throws IOException {
        //get terms to array
        int count = 0;
        String temp;
        while (count < searchTerms.length()) {
            temp = "";
            while (searchTerms.charAt(count) != '\n') {
                temp += "" + searchTerms.charAt(count);
                count++;
            }
            temp = temp.trim();
            if (temp.endsWith("-")) {
                temp = temp.substring(0, temp.length() - 1);
            }
            temp = temp.replace(' ', '+');
            terms.add(temp);
            count++;
        }
        System.out.println(terms.toString());
        //build html file
        StringBuilder htmlScript = new StringBuilder("<html><body>");
        for(String s : terms) {
            htmlScript.append("<p><a href=").append(baseURL).append(s).append(">").append(s.replace('+', ' ')).append("</a></p>");
        }
        htmlScript.append("</body></html>");
        FileWriter writer = new FileWriter("links.html");
        writer.write(htmlScript.toString());
        writer.close();
    }
}
