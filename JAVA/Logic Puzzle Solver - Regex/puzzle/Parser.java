package puzzle;

import java.io.Reader;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Scanner;
import java.util.ArrayList;
import static puzzle.EntityType.*;
import java.util.HashSet;

/** A sequence of Assertions and Questions parsed from a given file.
 *  @author Peterson Cheng */
class Parser {

    /** A new Parser, containing no assertions or questions. */
    private Parser() {
    }

    /** Returns a Parser that contains assertions and questions from
     *  READER. */
    static Parser parse(Reader reader) {
        ArrayList<String> inp = format(reader);
        Parser result = new Parser();
        for (int i = 0; i < inp.size(); i++) {
            String k = inp.get(i);
            if (isQuestion(k)) {
                _asked = true;
                parseQuestion(k);
            } else {
                if (_asked) {
                    String errmsg = "Asked questions before all assertions!";
                    throw new PuzzleException(errmsg);
                }
                parseAssertion(k);
            }
        }
        return result;
    }

    /** Transfer all information conveyed by the assertions I have
     *  read to Statement. */
    static void inform() {
        for (Statement s: Statement.statementList()) {
            Triple.add(s.getN0());
            if (!(s.isSingular())) {
                Triple.add(s.getN1());
            }
        }
    }

    /** Solves the puzzle from SOLVER. */
    static void solve(Solver solver) {
        Triple.populate();
        for (Statement s: Statement.statementList()) {
            //Triple.printTripleList();//
            if (s.isSingular()) {
                continue;
            } else if (s.isAssociation()) {
                solver.associate(s.getN0(), s.getN1());
            } else {
                solver.disassociate(s.getN0(), s.getN1());
            }
        }
    }

    /** Returns the number of assertions I have parsed. */
    int numAssertions() {
        return Statement.statementList().size();
    }

    /** Returns the text of assertion number K (numbering from 0), with extra
     *  spaces removed. */
    String getAssertion(int k) {
        return Statement.statementList().get(k).getMsg();
    }

    /** Returns the number of questions I have parsed. */
    int numQuestions() {
        return Question.questionList().size();
    }

    /** Return the text of question number K (numbering from 0), with extra
     *  spaces removed. */
    String getQuestion(int k) {
        return Question.questionList().get(k).getMsg();
    }

    /** Return the answer to question K, according to the information
     *  in solver S. */
    String getAnswer(int k, Solver s) {
        return Question.questionList().get(k).getAnswer(s);
    }

    /** ----------------------------------------------------
    /* Custom methods and fields.
    /*  --------------------------------------------------*/

    /** Returns true iff S is a question. */
    static boolean isQuestion(String s) {
        return s.contains("?");
    }

    /** Resets the lists. */
    static void reset() {
        Statement.clear();
        Question.clear();
    }

    /** Formats READER to have standardized spaces and returns it.*/
    static ArrayList<String> format(Reader reader) {
        Scanner inp = new Scanner(reader);
        inp = inp.useDelimiter(Pattern.compile("\\s+"));
        String text = "";
        ArrayList<String> result = new ArrayList<String>();
        while (inp.hasNext()) {
            String s = inp.next();
            if (s.contains(".") | s.contains("?")) {
                result.add(text + s);
                text = "";
            } else {
                text += s + " ";
            }
        }
        if (!(text.equals(""))) {
            throw new PuzzleException("Incorretly formatted input!");
        }
        return result;
    }

    /** Parses an assertion ASSERTS. */
    static void parseAssertion(String asserts) {
        Pattern p0 = Pattern.compile("([A-Z]\\w+) (lives in|does not live in)"
                                     + " the (\\w+) house[.]");
        Pattern p1 = Pattern.compile("The (\\w+) (lives in|does not live in)"
                                     + " the (\\w+) house[.]");
        Pattern p2 = Pattern.compile("([A-Z]\\w+) (is|is not) the (\\w+)[.]");
        Pattern p3 = Pattern.compile("([A-Z]\\w+) lives around here[.]");
        Pattern p4 = Pattern.compile("The (\\w+) lives around here[.]");
        Pattern p5 = Pattern.compile("There is a (\\w+) house[.]");
        Pattern[] p = {p0, p1, p2, p3, p4, p5};
        for (int i = 0; i < 6; i++) {
            Matcher m = p[i].matcher(asserts);
            if (m.matches()) {
                switch(i) {
                case 0: {
                    boolean b = positive(m.group(2));
                    NamedEntity n0 = NamedEntity.create(m.group(1), PERSON);
                    NamedEntity n1 = NamedEntity.create(m.group(3), COLOR);
                    Statement s = new Statement(n0, n1, b, m.group(0));
                    return;
                }
                case 1: {
                    boolean b = positive(m.group(2));
                    NamedEntity n0 = NamedEntity.create(m.group(1), OCCUPATION);
                    NamedEntity n1 = NamedEntity.create(m.group(3), COLOR);
                    Statement s = new Statement(n0, n1, b, m.group(0));
                    return;
                }
                case 2: {
                    boolean b = positive(m.group(2));
                    NamedEntity n0 = NamedEntity.create(m.group(1), PERSON);
                    NamedEntity n1 = NamedEntity.create(m.group(3), OCCUPATION);
                    Statement s = new Statement(n0, n1, b, m.group(0));
                    return;
                }
                case 3: {
                    NamedEntity n = NamedEntity.create(m.group(1), PERSON);
                    Statement s = new Statement(n, m.group(0));
                    return;
                }
                case 4: {
                    NamedEntity n = NamedEntity.create(m.group(1), OCCUPATION);
                    Statement s = new Statement(n, m.group(0));
                    return;
                }
                case 5: {
                    NamedEntity n = NamedEntity.create(m.group(1), COLOR);
                    Statement s = new Statement(n, m.group(0));
                    return;
                }
                default:
                }
            }
        }
        String errmsg = "Your input is faulty: " + asserts;
        throw new PuzzleException(errmsg);
    }

    /** Parses a question QUES. */
    static void parseQuestion(String ques) {
        Pattern p0 = Pattern.compile("Who is the (\\w+)[?]");
        Pattern p1 = Pattern.compile("Who lives in the (\\w+) house[?]");
        Pattern p2 = Pattern.compile("What does ([A-Z]\\w+) do[?]");
        Pattern p3 = Pattern.compile("What does the occupant of the (\\w+) "
                                     + "house do[?]");
        Pattern p4 = Pattern.compile("Where does the (\\w+) live[?]");
        Pattern p5 = Pattern.compile("Where does ([A-Z]\\w+) live[?]");
        Pattern p6 = Pattern.compile("What do you know about the (\\w+)[?]");
        Pattern p7 = Pattern.compile("What do you know about ([A-Z]\\w+)[?]");
        Pattern p8 = Pattern.compile("What do you know about the (\\w+) "
                                     + "house[?]");
        Pattern[] p = {p0, p1, p2, p3, p4, p5, p6, p7, p8};
        for (int i = 0; i < 9; i++) {
            Matcher m = p[i].matcher(ques);
            if (m.matches()) {
                String msg = m.group(0);
                NamedEntity n = NamedEntity.getID(m.group(1));
                if (n == null) {
                    throw new PuzzleException(m.group(1) + " does not exist");
                }
                switch(i) {
                case 0:
                case 1:
                    Question.create(n, PERSON, msg);
                    return;
                case 2:
                case 3:
                    Question.create(n, OCCUPATION, msg);
                    return;
                case 4:
                case 5:
                    Question.create(n, COLOR, msg);
                    return;
                case 6:
                case 7:
                case 8:
                    Question.create(n, KNOW, msg);
                    return;
                default:
                }
            }
        }
        String errmsg = "Your question is faulty: " + ques;
        throw new PuzzleException(errmsg);
    }

    /** Returns true if S asserts a positive or negative statement. */
    static boolean positive(String s) {
        return !(s.contains("not"));
    }

    /** Returns true if questions have been asked. */
    private static boolean _asked;

    /** Holds statements of associations and disassociations. */
    private static class Statement {

        /** Initializes a that holds two NamedEntities N0
         *  and N1, and whether or not is is an
         *  association B. Taken with MSG. */
        Statement(NamedEntity n0, NamedEntity n1,
                  boolean b, String msg) {
            _n0 = n0;
            _n1 = n1;
            _b = b;
            _msg = msg;
            _sList.add(this);
        }

        /** Initializes the statement of a single NamedEntity N
         *  with message MSG.*/
        Statement(NamedEntity n, String msg) {
            this(n, n, false, msg);
        }

        /** Returns N0. */
        public NamedEntity getN0() {
            return _n0;
        }

        /** Returns N1. */
        public NamedEntity getN1() {
            return _n1;
        }

        /** Returns the association/disassociation flag. */
        public boolean isAssociation() {
            return _b;
        }

        /** Returns true if the statment is singular. */
        public boolean isSingular() {
            return _n0.equals(_n1);
        }

        /** Returns the message. */
        public String getMsg() {
            return _msg;
        }

        /** Clears out sList. */
        static void clear() {
            _sList.clear();
        }

        /** Returns the list of association/disassociation flag. */
        public static ArrayList<Statement> statementList() {
            return _sList;
        }

        /** Holds the lists of statements. */
        private static ArrayList<Statement> _sList =
            new ArrayList<Statement>();

        /** Holds the first NamedEntity. */
        private NamedEntity _n0;

        /** Holds the second NamedEntity. */
        private NamedEntity _n1;

        /** Holds the association/disassociation flag. */
        private boolean _b;

        /** Holds the statement itself. */
        private String _msg;
    }

    /** Holds questions. */
    private static class Question {

        /** Constructs a question from MSG,
         *  asking about QUERY, about ENTITY. */
        private Question(NamedEntity entity, EntityType query,
                         String msg) {
            _entity = entity;
            _query = query;
            _msg = msg;
            _qList.add(this);
        }

        /** Returns a question from ENTITY, QUERY, and MSG. */
        public static Question create(NamedEntity entity,
                               EntityType query, String msg) {
            return new Question(entity, query, msg);
        }

        /** Returns the answer. Symmetrical questions (color
         *  asking for person or person asking for color)
         *  return the same answer for SOLV.*/
        String getAnswer(Solver solv) {
            //Triple.printTripleList();//
            Triple t = _entity.hasNamedEntity().toArray(new Triple[0])[0];
            if (_entity.hasNamedEntity().size() != 1) {
                reducetoOne(_entity, solv);
            }
            //Triple.printTripleList();//
            t = _entity.hasNamedEntity().toArray(new Triple[0])[0];
            return getAnswerString(t);
        }

        /** Returns the answer string for T. */
        String getAnswerString(Triple t) {
            EntityType eType = _entity.getType();
            String answer = "";
            String[] aList = t.answerGen();
            if ((_query == PERSON && eType == OCCUPATION)
                || (_query == OCCUPATION && eType == PERSON)) {
                answer = aList[1];
            } else if ((_query == PERSON && eType == COLOR)
                       || (_query == COLOR && eType == PERSON)) {
                answer = aList[2];
            } else if ((_query == OCCUPATION && eType == COLOR)
                       || (_query == COLOR && eType == OCCUPATION)) {
                answer = aList[3];
            } else if (_query == KNOW) {
                for (String s : aList) {
                    if (!(s.contains("#"))) {
                        answer = s;
                        return answer;
                    }
                }
            } else {
                String errmsg = "Incorrectly formmated question!";
                throw new PuzzleException(errmsg);
            }
            if (answer.contains("#")) {
                answer = "I don't know.";
            }
            return answer;
        }

        /** Returns the ambiguous count of a String S. */
        int ambCount(String s) {
            char c = "#".charAt(0);
            int n = 0;
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == c) {
                    n++;
                }
            }
            return n;
        }

        /** Returns the least ambiguous of two strings S1 and S2.*/
        Triple leastAmbig(Triple s1, Triple s2) {
            int n0 = ambCount(getAnswerString(s1));
            int n1 = ambCount(getAnswerString(s2));
            if (n0 <= n1) {
                if (getAnswerString(s1).equals("I don't know.")
                    || getAnswerString(s1).equals("Nothing.")) {
                    return s2;
                }
                return s1;
            } else {
                return s2;
            }
        }

        /** Handles the case where multiple possibilities remain
         *  but there is some useful information N. */
        void reducetoOne(NamedEntity n, Solver s) {
            Triple[] t = n.hasNamedEntity().toArray(new Triple[0]);
            //Triple.printTripleList();//
            //System.out.println(n.toString());
            HashSet<Triple> addback = new HashSet<Triple>();
            Triple one = leastAmbig(Question.agree(t),
                                    Question.agree2(t));
            for (Triple t0: t) {
                if (Triple.careRemove(n, t0)) {
                    addback.add(t0);
                }
                //addback.add(t0);
                //Triple.tList().remove(t0);
            }
            String s1 = getAnswerString(one);
            //System.out.println(s1);//
            if (s1.equals("I don't know.")) {
                HashSet<Triple> newAssertions = Triple.makeExclusive(s);
                if (newAssertions.size() != 0) {
                    Triple.tList().addAll(addback);
                    //Triple.printHash(addback, "addback");
                    //Triple.printHash(Triple.tList(), "TripleList");
                    for (Triple t0: newAssertions) {
                        Triple.makeExclusive(s, t0);
                    }
                } else {
                    Triple.tList().add(one);
                }
            } else {
                for (Triple t0: t) {
                    Triple.tList().remove(t0);
                }
                Triple.tList().add(one);
            }
        }

        /** Returns the least ambiguous triple of Triple array T. */
        static Triple agree(Triple[] t) {
            if (t.length == 0 ) {
                throw new PuzzleException("Possibilities removed");
            }
            NamedEntity p = t[0].getPerson();
            NamedEntity o = t[0].getOccupation();
            NamedEntity c = t[0].getColor();
            for (Triple t0: t) {
                if (!(t0.getPerson().isAnonymous())) {
                    p = t0.getPerson();
                }
                if (!(t0.getOccupation().isAnonymous())) {
                    o = t0.getOccupation();
                }
                if (!(t0.getColor().isAnonymous())) {
                    c = t0.getOccupation();
                }
            }

            boolean pflag, oflag, cflag;
            pflag = oflag = cflag = false;
            for (Triple t0: t) {
                if (!(t0.getPerson().equals(p))) {
                    if (!(t0.getPerson().isAnonymous())) {
                        pflag = true;
                    }
                }
                if (!(t0.getOccupation().equals(o))) {
                    if (!(t0.getOccupation().isAnonymous())) {
                        oflag = true;
                    }
                }
                if (!(t0.getColor().equals(c))) {
                    if (!(t0.getColor().isAnonymous())) {
                        cflag = true;
                    }
                }
            }
            if (pflag) {
                String s = NamedEntity.makeName("p");
                p = NamedEntity.create(s, PERSON);
            }
            if (oflag) {
                String s = NamedEntity.makeName("o");
                o = NamedEntity.create(s, OCCUPATION);
            }
            if (cflag) {
                String s = NamedEntity.makeName("c");
                c = NamedEntity.create(s, COLOR);
            }
            return new Triple(p, o, c);
        }

        /** Returns the least ambiguous triple of Triple array T. */
        static Triple agree2(Triple[] t) {
            NamedEntity p = t[0].getPerson();
            NamedEntity o = t[0].getOccupation();
            NamedEntity c = t[0].getColor();
            boolean pflag, oflag, cflag;
            pflag = oflag = cflag = false;
            for (Triple t0: t) {
                if (!(t0.getPerson().equals(p))) {
                    pflag = true;
                }
                if (!(t0.getOccupation().equals(o))) {
                    oflag = true;
                }
                if (!(t0.getColor().equals(c))) {
                    cflag = true;
                }
            }
            if (pflag) {
                String s = NamedEntity.makeName("p");
                p = NamedEntity.create(s, PERSON);
            }
            if (oflag) {
                String s = NamedEntity.makeName("o");
                o = NamedEntity.create(s, OCCUPATION);
            }
            if (cflag) {
                String s = NamedEntity.makeName("c");
                c = NamedEntity.create(s, COLOR);
            }
            return new Triple(p, o, c);
        }

        /** Returns the original question. */
        String getMsg() {
            return _msg;
        }

        /** Returns the list of questions. */
        public static ArrayList<Question> questionList() {
            return _qList;
        }

        /** Clears out sList. */
        static void clear() {
            _qList.clear();
        }

        /** Holds the entity the question is asking about. */
        private NamedEntity _entity;

        /** Holds the type of answer the question is looking for. */
        private EntityType _query;

        /** Holds the question itself in String format. */
        private String _msg;

        /** Holds the question list. */
        private static ArrayList<Question> _qList =
            new ArrayList<Question>();
    }


}

