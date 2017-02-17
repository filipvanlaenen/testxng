<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:transform version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template match="/Foo">
    <Foo>
      <xsl:text>A Bar with </xsl:text>
      <xsl:value-of select="Bar"/>
      <xsl:text> at the root</xsl:text>
    </Foo>
  </xsl:template>

  <xsl:template match="Foo">
    <Foo>
      <xsl:text>A Bar with </xsl:text>
      <xsl:value-of select="Bar"/>
    </Foo>
  </xsl:template>
  
  <xsl:template match="FooWithAttribute">
    <Foo>
      <xsl:attribute name="Bar">
        <xsl:text><xsl:value-of select="Bar"/></xsl:text>
      </xsl:attribute>
      <xsl:text>A Bar as an attribute</xsl:text>
    </Foo>
  </xsl:template>

  <xsl:template match="Foo" mode="qux">
    <Foo>
      <xsl:text>A Bar with </xsl:text>
      <xsl:value-of select="Bar"/>
      <xsl:text> in qux</xsl:text>
    </Foo>
  </xsl:template>

</xsl:transform>