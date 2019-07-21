package com.arsvechkarev.timerx.format;

import static com.arsvechkarev.timerx.Constants.Patterns.PATTERN_HAS_HOURS;
import static com.arsvechkarev.timerx.Constants.Patterns.PATTERN_HAS_MILLIS;
import static com.arsvechkarev.timerx.Constants.Patterns.PATTERN_HAS_MINUTES;
import static com.arsvechkarev.timerx.Constants.Patterns.PATTERN_HAS_SECONDS;
import static com.arsvechkarev.timerx.Constants.TimeUnits.HOURS;
import static com.arsvechkarev.timerx.Constants.TimeUnits.MILLIS;
import static com.arsvechkarev.timerx.Constants.TimeUnits.MINUTES;
import static com.arsvechkarev.timerx.Constants.TimeUnits.SECONDS;

import com.arsvechkarev.timerx.exceptions.IllegalSymbolsCombinationException;
import com.arsvechkarev.timerx.exceptions.IllegalSymbolsPositionException;
import com.arsvechkarev.timerx.exceptions.NoNecessarySymbolsException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

  private Semantic semantic;

  private Validator(Semantic semantic) {
    this.semantic = semantic;
  }

  public static void check(Semantic semantic) {
    Validator validator = new Validator(semantic);
    validator.checkFormat(semantic.getFormat());
  }

  private void checkFormat(String parseFormat) {
    int formatOccurs = positionalCheckOf(parseFormat);
    if (formatOccurs == 0) {
      throwNoSymbolsEx();
    } else {
      executeCombinationCheck();
    }
  }

  private int positionalCheckOf(String parseFormat) {
    int formatOccurs = 0;
    for (int type = 0; type < 4; type++) {
      String strPattern = nextPatternOf(type);
      Pattern pattern = Pattern.compile(strPattern);
      Matcher matcher = pattern.matcher(parseFormat);
      int counter = 0;
      while (matcher.find()) {
        updateAppropriateField(type, matcher.group().length());
        formatOccurs++;
        counter++;
        if (counter > 1) {
          throwIncorrectPositionEx();
        }
      }
    }
    return formatOccurs;
  }

  private String nextPatternOf(int pos) {
    switch (pos) {
      case 0:
        return PATTERN_HAS_HOURS;
      case 1:
        return PATTERN_HAS_MINUTES;
      case 2:
        return PATTERN_HAS_SECONDS;
      case 3:
        return PATTERN_HAS_MILLIS;
      default:
        throw new IllegalStateException("No pattern to match");
    }
  }

  private void updateAppropriateField(int type, int count) {
    if (type == 0) {
      semantic.setHoursCount(count);
    } else if (type == 1) {
      semantic.setMinutesCount(count);
    } else if (type == 2) {
      semantic.setSecondsCount(count);
    } else if (type == 3) {
      semantic.setMillisCount(count);
    } else {
      throw new IllegalStateException("No number to match");
    }
  }

  private void executeCombinationCheck() {
    boolean hasHours = semantic.has(HOURS);
    boolean hasMinutes = semantic.has(MINUTES);
    boolean hasSeconds = semantic.has(SECONDS);
    boolean hasMillis = semantic.has(MILLIS);
    if (hasHours) {
      if ((hasSeconds || hasMillis) && !hasMinutes) {
        throwCombinationEx();
      } else if (hasMinutes && hasMillis && !hasSeconds) {
        throwCombinationEx();
      }
    } else {
      if (hasMinutes && hasMillis && !hasSeconds) {
        throwCombinationEx();
      }
    }
  }

  private void throwIncorrectPositionEx() {
    throw new IllegalSymbolsPositionException(
        "Some check symbol contains many times in format");
  }

  private void throwNoSymbolsEx() {
    throw new NoNecessarySymbolsException("Format hasn't any necessary symbols");
  }

  private void throwCombinationEx() {
    throw new IllegalSymbolsCombinationException(
        "Different symbols in format has incompatible order");
  }
}
