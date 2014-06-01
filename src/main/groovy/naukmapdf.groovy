@Grab(group='org.ccil.cowan.tagsoup',
        module='tagsoup', version='1.2' )

class NaukmaGrabber {
    static File logfile = new File("/Users/user/naukma/grabbing.log")
    static String basePath = "/Users/user/naukma/pdfs"
    static File baseDir = new File(basePath)
    static int docCount = 0;
    static def tagsoupParser = new org.ccil.cowan.tagsoup.Parser() as Object
    static def slurper = new XmlSlurper(tagsoupParser)

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis()
        if(!logfile.exists())logfile.createNewFile();
        if(!baseDir.exists())baseDir.mkdir();


        logfile.withWriterAppend {f -> f << new Date().getDateTimeString() +" __ " + "Start pdfs grabbing"+"\n"}
        def text = ("http://nz.ukma.edu.ua/index.php?option=com_content&task=section&id=10&Itemid=47").toURL().getText();
        def xml = slurper.parseText(text)

        xml."**".findAll { it.@class.toString().contains("category")}.eachWithIndex {
            elem, index ->
                String year = elem.text().replaceAll("\\s+","")
                processYear(year, elem.@href)
        }

        long endTime = System.currentTimeMillis()
        logfile.withWriterAppend {f -> f << new Date().getDateTimeString() +" __ " + "TIME SPENT: "+ (endTime-startTime)/1000.0 + " seconds"+"\n"}
        logfile.withWriterAppend {f -> f << new Date().getDateTimeString() +" __ " +"End grabbing pdfs"+"\n"}
        logfile.withWriterAppend {f -> f << new Date().getDateTimeString() +" __ " +"PDFs collected: " + docCount + "\n"}

    }
    public static void processYear(year, link){
        String yearPath = basePath + "/" + year;
        File yearDir = new File(yearPath)
        if(!yearDir.exists()) yearDir.mkdir()
        logfile.withWriterAppend {f -> f << new Date().getDateTimeString() +" __ " + "Start year: "+ year +"\n"}
        def text = (link).toURL().getText("windows-1251");
        def xml = slurper.parseText(text)
        xml."**".findAll { it.@class.toString().contains("sectiontableentry")}.eachWithIndex {
            elem, index ->
                String topicText = elem.td.text().replaceAll("\\s+","_");
                String linkTo = elem.td.a.@href;
                processTopic(yearPath, topicText, linkTo)
        }
    }
    public static void processTopic(yearPath, topic, link){
        String topicPath = yearPath + "/" + topic;
        File topicDir = new File(topicPath)
        if(!topicDir.exists()) topicDir.mkdir()
        logfile.withWriterAppend {f -> f << new Date().getDateTimeString() +" __ " + "Start topic: "+ topic +"\n"}
        def text = (link).toURL().getText("windows-1251");
        def xml = slurper.parseText(text)
        xml."**".findAll { it.@target.equals("_blank") && it.text().toString().contains("Повний текст")}.eachWithIndex {
            elem, index ->
                String linkTo = elem.@href
                if(linkTo.endsWith(".pdf")){
                    String filename = linkTo.substring(linkTo.lastIndexOf("/") + 1)
                    String filePath = topicPath + "/" + filename
                    logfile.withWriterAppend {f -> f << new Date().getDateTimeString() +" __ " + "Save document: "+ filePath +"\n"}
                    saveFileFromURL(linkTo, filePath)
                    docCount++
                } else {
                   processDocument(topicPath, linkTo)
                }
        }
    }
    public static void processDocument(topicPath, link){
        logfile.withWriterAppend {f -> f << new Date().getDateTimeString() +" __ " + "Start document: "+ link +"\n"}
        try{
            def text = (link).toURL().getText();
            def xml = slurper.parseText(text)
            xml."**".findAll { it.@target.equals("_blank") && it.@href.toString().endsWith(".pdf") && it.text().toString().endsWith(".pdf")}.eachWithIndex {
                elem, index ->
                    String linkTo = elem.@href
                    if(linkTo.endsWith(".pdf")){
                        String filename = linkTo.substring(linkTo.lastIndexOf("/") + 1)
                        String filePath = topicPath + "/" + filename
                        logfile.withWriterAppend {f -> f << new Date().getDateTimeString() +" __ " + "Save document: "+ filePath +"\n"}
                        saveFileFromURL("http://www.ekmair.ukma.kiev.ua" + linkTo, filePath)
                        docCount++
                    }
            }
        }  catch (exc){
            logfile.withWriterAppend {f -> f << new Date().getDateTimeString() +" __ " + "Error: "+ exc +"\n"}
        }

    }
    public static saveFileFromURL(link, file){
        if(!new File(file).exists()){
            try{
                def f = new File(file).newOutputStream()
                f << new URL(link).openStream()
                f.close()
            } catch (exc){
                logfile.withWriterAppend {f -> f << new Date().getDateTimeString() +" __ " + "Error: "+ exc +"\n"}
            }
        }

    }
}



