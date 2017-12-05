package de.vaplus.api.entity;

public interface Faq extends StatusBase{

	String getQuestion();

	String getAnswer();

	String getQuestionUser();

	String getAnswerUser();

	String getTags();

	String[] getTagList();


}
