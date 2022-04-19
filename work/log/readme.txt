1) JUL -
//  jdk8:  ��������� ��������� � ����� $java_home/jre/lib/logging.properties
//  jdk11: ��������� ��������� � ����� $java_home/conf/logging.properties

2) jboss logging - log4j, JUL
Logger SLF4J ������� �� jbosslogging, ������� �������� �� ��������� JDK logger (JUL)
��� ���������� ���������� log4j � ����� ������������ log4j.properties
������������ ������������� �� log4j.
����� ��������� � ������ logback, �� ��� ����� ����� ��� SLF4J.

���� log4j � pom
    <dependency>
      <groupId>log4j</groupId>
      <artifactId>log4j</artifactId>
      <version>1.2.17</version>
    </dependency>

���� logback
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <version>1.2.5</version>
    </dependency>

����� JUL

3) logback - logback.xml, BaseConfig
���� logback.xml �� ������, �� ������������ ������� ������������ �� ���������.

4) ��������� ���������� ������ logback - 1.2.5
������� � 1.2.6 ��� ������� ������� ��������� ������:

Parser configuration error occurred javax.xml.parsers.ParserConfigurationException: 
SAX feature 'http://xml.org/sax/features/external-general-entities' not supported.

    private SAXParser buildSaxParser() throws JoranException {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setValidating(false);
            //spf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            spf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            spf.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
            spf.setNamespaceAware(true);
            return spf.newSAXParser();
        } catch (Exception pce) {
            String errMsg = "Parser configuration error occurred";
            addError(errMsg, pce);
            throw new JoranException(errMsg, pce);
        }
    }

������ ��������� ��� ������������� xmlparserv2.jar
    <dependency>
      <groupId>com.oracle.database.xml</groupId>
      <artifactId>xmlparserv2</artifactId>
      <version>${ojdbc.version}</version>
    </dependency>

�� ������ 1.2.6 ��� �������� ���������.

