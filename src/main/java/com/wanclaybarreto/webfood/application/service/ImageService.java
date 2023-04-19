package com.wanclaybarreto.webfood.application.service;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wanclaybarreto.webfood.util.IOUtils;

@Service
public class ImageService {
	
	@Value("${webfood.files.logotipo}")
	private String logotiposDir;
	
	@Value("${webfood.files.itemcardapio}")
	private String itensCardapioDir;
	
	@Value("${webfood.files.categoria}")
	private String categoriasDir;
	
	public void uploadLogotipo(MultipartFile multipartFile, String fileName) {
		try {
			IOUtils.copy(multipartFile.getInputStream(), fileName, logotiposDir);
		} catch (IOException e) {
			throw new ApplicationServiceException(e);
		}
	}

	public void uploadImagemItemCardapio(MultipartFile multipartFile, String fileName) {
		try {
			IOUtils.copy(multipartFile.getInputStream(), fileName, itensCardapioDir);
		} catch (IOException e) {
			throw new ApplicationServiceException(e);
		}
	}
	
	public byte[] getBytes(String type, String imgName) {
		
		try {
			
			String dir;
			
			if ("logotipo".equals(type)) {
				dir = logotiposDir;
			} else if ("itemcardapio".equals(type)) {
				dir = itensCardapioDir;
			} else if ("categoria".equals(type)) {
				dir = categoriasDir;
			} else {
				throw new Exception(type + " não é um tipo de imagem válido.");
			}
			
			return IOUtils.getBytes(Paths.get(dir, imgName));
			
		} catch (Exception e) {
			
			throw new ApplicationServiceException(e);
			
		}
	}
	
}
