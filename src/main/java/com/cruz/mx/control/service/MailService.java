package com.cruz.mx.control.service;

import com.cruz.mx.control.dao.beans.PersonalBean;
import java.io.File;

public interface MailService {
        
	public void sendEmail(final PersonalBean personal, final File archivo, final String destino);
        
}
