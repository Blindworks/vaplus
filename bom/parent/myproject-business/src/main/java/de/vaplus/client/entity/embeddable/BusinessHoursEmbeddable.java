package de.vaplus.client.entity.embeddable;

import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import de.vaplus.api.entity.embeddable.BusinessHours;

@Embeddable
public class BusinessHoursEmbeddable implements BusinessHours{
	

	@Column(name="opening")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Time open;

	@Column(name="closing")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Time close;

}
