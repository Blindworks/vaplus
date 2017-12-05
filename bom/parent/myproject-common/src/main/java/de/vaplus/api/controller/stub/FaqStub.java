package de.vaplus.api.controller.stub;

import javax.persistence.Transient;

import de.vaplus.api.entity.Faq;

public class FaqStub extends StatusBaseStub{

	private static final long serialVersionUID = 496259407380067044L;

	private String question;

	private String answer;

	private String questionUser;
	
	private String answerUser;
	
	private String tags;
	
	public FaqStub(){}
	
	public FaqStub(Faq obj) {
		super(obj);

		question = obj.getQuestion();
		answer = obj.getAnswer();
		questionUser = obj.getQuestionUser();
		answerUser = obj.getAnswerUser();
		tags = obj.getTags();
	}

//	@Override
	public String getQuestion() {
		return question;
	}

//	@Override
	public String getAnswer() {
		return answer;
	}

//	@Override
	public String getQuestionUser() {
		return questionUser;
	}

//	@Override
	public String getAnswerUser() {
		return answerUser;
	}

//	@Override
	public String getTags() {
		return tags;
	}

//	@Override
	@Transient
	public String[] getTagList() {
		String[] tagList = new String[0];
		
		if(tags != null)
			tagList = tags.split(",");
		
		return tagList;
	}
}
