package com.taobao.cun.admin.web.partner.module.util;

import java.util.List;

import com.google.common.collect.Lists;
import com.taobao.cun.admin.web.vo.FileUploadVo;
import com.taobao.cun.crius.train.dto.FileUploadDto;

public class FileUploadVoConverter {

	public static FileUploadDto convertToFileUploadDto(FileUploadVo vo) {
		if (null == vo) {
			return null;
		}
		FileUploadDto dto = new FileUploadDto();
		dto.setAttachementId(vo.getAttachementId());
		dto.setFsId(vo.getFsId());
		dto.setFileType(vo.getFileType());
		dto.setTitle(vo.getTitle());
		return dto;
	}

	public static List<FileUploadDto> convertToFileUploadDtoList(List<FileUploadVo> voList) {
		if (null == voList) {
			return null;
		}

		List<FileUploadDto> list = Lists.newArrayList();
		for (FileUploadVo vo : voList) {
			FileUploadDto dto = convertToFileUploadDto(vo);
			if (null != dto) {
				list.add(dto);
			}
		}
		return list;
	}

}
