package com.web.common.web.common.util.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.web.common.web.common.util.poi.ExcelUtil;

/**
 * @author: chengweixiong@uworks.cc
 * @date: 2016年5月27日 下午12:18:20
 */
public class XmlUtils {
  private static String charset = "UTF-8";
  private static final Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

  /**
   * 转成xml字符串
   * 
   * @param request
   * @return
   */
  public static String toXml(Object request) {
    StringWriter writer = null;
    try {
      // 设置商户编号
      JAXBContext context = JAXBContext.newInstance(request.getClass());
      Marshaller marshaller = context.createMarshaller();
      // 设置编码格式
      marshaller.setProperty(Marshaller.JAXB_ENCODING, charset);
      // 设置是否格式化
      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
      // 省略xm头声明信息
      // marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true);
      writer = new StringWriter();
      marshaller.marshal(request, new StreamResult(writer));
      String result = writer.toString();
      return result;
    } catch (Exception e) {
      logger.error(e.getMessage(), e);
      throw new RuntimeException(e);
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch (IOException e) {
          logger.error(e.getMessage(), e);
          throw new RuntimeException(e);
        }
      }
    }
  }

  /**
   * xml转成对象
   * 
   * @param xml
   * @param c
   * @return
   */
  @SuppressWarnings("unchecked")
  public  static <T> T toObject(String xml, Class<T> c) {
    if (xml == null || "".equals(xml.trim())) {
      return null;
    }
    StringReader reader = null;
    try {
      JAXBContext context = JAXBContext.newInstance(c);
      javax.xml.bind.Unmarshaller unmarshaller = context.createUnmarshaller();
      reader = new StringReader(xml);
      T result = (T) unmarshaller.unmarshal(new StreamSource(reader));
      return result;
    } catch (Exception e) {
      e.printStackTrace();;
      throw new RuntimeException(e.getMessage(), e);
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
          e.printStackTrace();
          throw new RuntimeException(e);
        }
      }
    }
  }

}
