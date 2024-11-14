package org.dci.assecorassessmentbackend.model;

/**
 * Enum representing colors with associated code and display name.
 */
public enum Color {
  BLAU(1, "blau"),
  GRUEN(2, "grün"),
  VIOLETT(3, "violett"),
  ROT(4, "rot"),
  GELB(5, "gelb"),
  TUERKIS(6, "türkis"),
  WEISS(7, "weiß");

  private final int code;
  private final String displayName;

  /**
   * Constructor for Color enum.
   * @param code Numeric code associated with the color.
   * @param displayName Display name of the color.
   */
  Color(int code, String displayName) {
    this.code = code;
    this.displayName = displayName;
  }

  /**
   * Gets the numeric code of the color.
   * @return Integer code associated with the color.
   */
  public int getCode() {
    return code;
  }

  /**
   * Gets the display name of the color.
   * @return String representing the display name of the color.
   */
  public String getDisplayName() {
    return displayName;
  }

  /**
   * Retrieves a Color enum by its numeric code.
   * @param code Integer code associated with the color.
   * @return Color enum matching the specified code.
   * @throws IllegalArgumentException if no color matches the given code.
   */
  public static Color fromCode(int code) {
    for (Color color : Color.values()) {
      if (color.getCode() == code) {
        return color;
      }
    }
    throw new IllegalArgumentException("Unknown color code: " + code);
  }

  /**
   * Retrieves a Color enum by its display name.
   * @param displayName String display name of the color.
   * @return Color enum matching the specified display name.
   * @throws IllegalArgumentException if no color matches the given display name.
   */
  public static Color fromDisplayName(String displayName) {
    for (Color color : Color.values()) {
      if (color.getDisplayName().equalsIgnoreCase(displayName)) {
        return color;
      }
    }
    throw new IllegalArgumentException("Unknown color display name: " + displayName);
  }
}
