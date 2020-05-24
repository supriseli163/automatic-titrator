package com.jh.automatic_titrator.ui.chat.highlight;


public final class Range {

	public float from;
	public float to;

	public Range(float from, float to) {
		this.from = from;
		this.to = to;
	}

	/**
	 * Returns true if this range contains (if the value is in between) the given value, false if not.
	 * 
	 * @param value
	 * @return
	 */
	public boolean contains(float value) {

		if (value > from && value <= to)
			return true;
		else
			return false;
	}

	public boolean isLarger(float value) {
		return value > to;
	}

	public boolean isSmaller(float value) {
		return value < from;
	}
}