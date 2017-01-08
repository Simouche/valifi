package com.mlykotom.mlyked;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.Patterns;

import java.util.regex.Pattern;


/**
 * Handles validating on simple string fields
 */
@SuppressWarnings("unused")
public class ValidatedField extends ValidatedBaseField<String> {
	public ValidatedField() {
		super();
	}


	public ValidatedField(String defaultValue) {
		super(defaultValue);
	}


	@Override
	public String toString() {
		return mValue;
	}


	/**
	 * Checking for specific type if value is empty.
	 * Used for checking if empty is allowed.
	 *
	 * @param actualValue value when checking
	 * @return true when value is empty, false when values is not empty (e.g for String, use isEmpty())
	 * @see #mCallback
	 */
	@Override
	protected boolean whenThisFieldIsEmpty(@NonNull String actualValue) {
		return actualValue.isEmpty();
	}


	@Override
	protected String convertValueToString() {
		return mValue;
	}


	// ------------------ PATTERN VALIDATOR ------------------ //


	/**
	 * @see #addPatternValidator(String, Pattern)
	 */
	public ValidatedField addPatternValidator(@StringRes int errorResource, final Pattern pattern) {
		String errorMessage = Mlyked.getContext().getString(errorResource);
		return addPatternValidator(errorMessage, pattern);
	}


	/**
	 * Validates any pattern specified with parameter
	 *
	 * @param errorMessage specifies error message to be shown
	 * @param pattern      validates this pattern
	 * @return this, so validators can be chained
	 */
	public ValidatedField addPatternValidator(String errorMessage, final Pattern pattern) {
		addCustomValidator(errorMessage, new PropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) {
				return value != null && pattern.matcher(value).matches();
			}
		});
		return this;
	}


	// ------------------ NOT EMPTY VALIDATOR ------------------ //


	/**
	 * Validates emptiness of the value
	 *
	 * @return this, so validators can be chained
	 */
	public ValidatedField addNotEmptyValidator() {
		return addNotEmptyValidator(Mlyked.getErrorRes(Mlyked.Builder.ERROR_RES_NOT_EMPTY));
	}


	public ValidatedField addNotEmptyValidator(@StringRes int errorResource) {
		String errorMessage = Mlyked.getContext().getString(errorResource);
		return addNotEmptyValidator(errorMessage);
	}


	/**
	 * @param errorMessage specifies error message to be shown
	 * @return this, so validators can be chained
	 */
	public ValidatedField addNotEmptyValidator(String errorMessage) {
		return addMinLengthValidator(errorMessage, 1);
	}


	// ------------------ MIN LENGTH VALIDATOR ------------------ //


	/**
	 * Validates min length of string
	 *
	 * @param minLength must be larger or equal
	 * @return this, so validators can be chained
	 */
	public ValidatedField addMinLengthValidator(int minLength) {
		return addMinLengthValidator(Mlyked.getErrorRes(Mlyked.Builder.ERROR_RES_LENGTH_MIN), minLength);
	}


	public ValidatedField addMinLengthValidator(@StringRes int errorResource, int minLength) {
		String errorMessage = Mlyked.getContext().getString(errorResource, minLength);
		return addMinLengthValidator(errorMessage, minLength);
	}


	/**
	 * @param errorMessage specifies error message to be shown
	 * @param minLength    must be larger or equal
	 * @return this, so validators can be chained
	 */
	public ValidatedField addMinLengthValidator(String errorMessage, final int minLength) {
		return addRangeLengthValidator(errorMessage, minLength, Integer.MAX_VALUE);
	}


	// ------------------ EXACT LENGTH VALIDATOR ------------------ //


	public ValidatedField addExactLengthValidator(int exactLength) {
		return addExactLengthValidator(Mlyked.getErrorRes(Mlyked.Builder.ERROR_RES_LENGTH_EXACT), exactLength);
	}


	public ValidatedField addExactLengthValidator(@StringRes int errorResource, int exactLength) {
		String errorMessage = Mlyked.getContext().getString(errorResource, exactLength);
		return addExactLengthValidator(errorMessage, exactLength);
	}


	// ------------------ MAX LENGTH VALIDATOR ------------------ //


	public ValidatedField addExactLengthValidator(String errorMessage, final int exactLength) {
		return addRangeLengthValidator(errorMessage, exactLength, exactLength);
	}


	public ValidatedField addMaxLengthValidator(int maxLength) {
		return addMaxLengthValidator(Mlyked.getErrorRes(Mlyked.Builder.ERROR_RES_LENGTH_MAX), maxLength);
	}


	public ValidatedField addMaxLengthValidator(@StringRes int errorResource, int maxLength) {
		String errorMessage = Mlyked.getContext().getString(errorResource, maxLength);
		return addMaxLengthValidator(errorMessage, maxLength);
	}


	public ValidatedField addMaxLengthValidator(String errorMessage, final int maxLength) {
		return addRangeLengthValidator(errorMessage, 0, maxLength);
	}


	// ------------------ RANGE VALIDATOR ------------------ //


	public ValidatedField addRangeLengthValidator(int minLength, int maxLength) {
		return addRangeLengthValidator(Mlyked.getErrorRes(Mlyked.Builder.ERROR_RES_LENGTH_RANGE), minLength, maxLength);
	}


	public ValidatedField addRangeLengthValidator(@StringRes int errorResource, int minLength, int maxLength) {
		String errorMessage = Mlyked.getContext().getString(errorResource, minLength, maxLength);
		return addRangeLengthValidator(errorMessage, minLength, maxLength);
	}


	public ValidatedField addRangeLengthValidator(String errorMessage, final int minLength, final int maxLength) {
		if(minLength > 0) {
			mIsEmptyAllowed = false;
		}

		addCustomValidator(errorMessage, new PropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) {
				int length = value != null ? value.trim().length() : 0;
				return length >= minLength && length <= maxLength;
			}
		});
		return this;
	}


	// ------------------ EMAIL VALIDATOR ------------------ //


	/**
	 * Validates email addresses based on Android's {@link Patterns#EMAIL_ADDRESS}
	 * Has default error message
	 *
	 * @return this, so validators can be chained
	 */
	public ValidatedField addEmailValidator() {
		return addEmailValidator(Mlyked.getErrorRes(Mlyked.Builder.ERROR_RES_EMAIL));
	}


	public ValidatedField addEmailValidator(@StringRes int errorResource) {
		String errorMessage = Mlyked.getContext().getString(errorResource);
		return addEmailValidator(errorMessage);
	}


	/**
	 * Validates email addresses based on Android's {@link Patterns#EMAIL_ADDRESS}
	 *
	 * @param errorMessage specifies error message to be shown
	 * @return this, so validators can be chained
	 */
	public ValidatedField addEmailValidator(String errorMessage) {
		addCustomValidator(errorMessage, new PropertyValidator<String>() {
			@Override
			public boolean isValid(@Nullable String value) {
				return value != null && Mlyked.getPattern(Mlyked.Builder.PATTERN_EMAIL).matcher(value).matches();
			}
		});
		return this;
	}


	// ------------------ PHONE VALIDATOR ------------------ //


	/**
	 * Validates US or Czech phone numbers
	 * Has default error message
	 *
	 * @return this, so validators can be chained
	 */
	public ValidatedField addPhoneValidator() {
		return addPhoneValidator(Mlyked.getErrorRes(Mlyked.Builder.ERROR_RES_PHONE));
	}


	public ValidatedField addPhoneValidator(@StringRes int errorResource) {
		String errorMessage = Mlyked.getContext().getString(errorResource);
		return addPhoneValidator(errorMessage);
	}


	/**
	 * Validates US or Czech phone numbers
	 *
	 * @param errorMessage specifies error message to be shown
	 * @return this, so validators can be chained
	 */
	public ValidatedField addPhoneValidator(String errorMessage) {
		addPatternValidator(errorMessage, Mlyked.getPattern(Mlyked.Builder.PATTERN_PHONE));
		return this;
	}


	// ------------------ PASSWORD VALIDATOR ------------------ //


	public ValidatedField addPasswordValidator() {
		return addPasswordValidator(Mlyked.getErrorRes(Mlyked.Builder.ERROR_RES_PASSWORD));
	}


	public ValidatedField addPasswordValidator(@StringRes int errorResource) {
		String errorMessage = Mlyked.getContext().getString(errorResource);
		return addPasswordValidator(errorMessage);
	}


	public ValidatedField addPasswordValidator(String errorMessage) {
		addPatternValidator(errorMessage, Mlyked.getPattern(Mlyked.Builder.PATTERN_PASSWORD));
		return this;
	}


	// ------------------ USERNAME VALIDATOR ------------------ //


	public ValidatedField addUsernameValidator() {
		return addUsernameValidator(Mlyked.getErrorRes(Mlyked.Builder.ERROR_RES_USERNAME));
	}


	public ValidatedField addUsernameValidator(@StringRes int errorMessage) {
		return addUsernameValidator(Mlyked.getContext().getString(errorMessage));
	}


	public ValidatedField addUsernameValidator(String errorMessage) {
		addPatternValidator(errorMessage, Mlyked.getPattern(Mlyked.Builder.PATTERN_USERNAME));
		return this;
	}
}