package uk.co.controlnetworksolutions.elitedali2.utils;

import javax.baja.util.Lexicon;

public final class Lex {
   public static final Lexicon moduleLexicon = Lexicon.make("elitedali2");

   public static final String getText(String var0) {
      return moduleLexicon.getText(var0);
   }

   public static final Lexicon getLexicon() {
      return moduleLexicon;
   }
}
