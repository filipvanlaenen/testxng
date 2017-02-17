<?xml version="1.0" encoding="ISO-8859-1"?>
<!-- $Id: FooBar.xslt 52 2010-04-02 21:01:18Z filipvanlaenen $ -->
<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  
  <xsl:template match="Foo">
    <Foo>
      <xsl:attribute name="Bar">
        <xsl:text><xsl:value-of select="Bar"/></xsl:text>
      </xsl:attribute>
      <xsl:text>Qux</xsl:text>
    </Foo>
  </xsl:template>

</xsl:transform>
