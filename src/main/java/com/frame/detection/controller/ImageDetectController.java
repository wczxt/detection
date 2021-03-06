
package com.frame.detection.controller;

import com.frame.detection.constants.ViewModelConstants;
import com.frame.detection.service.ImageConsultService;
import com.frame.detection.service.ImageDetectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * controller of image detect
 *
 * @author anylots
 * @version $Id: ImageDetectController.java, v 0.1 2020年06月26日 18:56 anylots Exp $
 */
@Controller
public class ImageDetectController {

    /**
     * logger of ImageDetectController
     */
    private Logger logger = LoggerFactory.getLogger(ImageDetectController.class);

    /**
     * service of imageDetect
     */
    @Autowired
    private ImageDetectService imageDetectService;

    /**
     * service of imageConsult
     */
    @Autowired
    private ImageConsultService imageConsultService;

    @RequestMapping(value ="/",method = RequestMethod.GET)
    public String index(){
        return "detect.html";
    }

    /**
     * detect out
     *
     * @return
     */
    @RequestMapping(value = "/detect", method = RequestMethod.GET)
    public String detect() {

        return ViewModelConstants.DETECT;
    }


    /**
     * detect out
     *
     * @return
     */
    @RequestMapping(value = "/detectConsult", method = RequestMethod.GET)
    public String detectConsult() {

        return ViewModelConstants.DETECT_CONSULT;
    }

    /**
     * detect out
     *
     * @param imageLink
     * @return
     */
    @RequestMapping(value = "/detectImage", method = RequestMethod.POST)
    public ModelAndView detectOut(String imageLink, MultipartFile imageFile) {

        // step 1. detect image
        String detectOut = StringUtils.isEmpty(imageFile.getOriginalFilename()) ? imageDetectService.detectByUrl(imageLink)
                : imageDetectService.detectByFile(imageFile);

        // step 2. assemble modelAndView

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ViewModelConstants.DETECT_OUT);
        String[] result = detectOut.split(" ",2);
        //modelAndView.addObject(ViewModelConstants.DETECT_OUT_IMAGE, detectOut);
        modelAndView.addObject(ViewModelConstants.DETECT_OUT_IMAGE_percent, result[0]);
        modelAndView.addObject(ViewModelConstants.DETECT_OUT_words, result[1]);

        // step 3. return detect result page
        return modelAndView;
    }

    /**
     * detect consult
     *
     * @param imageLink
     * @return
     */
    @RequestMapping(value = "/consultImage", method = RequestMethod.POST)
    public ModelAndView consultImage(String imageLink) throws Exception {

        // step 1. detect image
        //base64格式的图片+识别的文字
        String detectOut = imageConsultService.detectInQueue(imageLink);


        // step 2. assemble modelAndView
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(ViewModelConstants.DETECT_OUT);
        String[] result = detectOut.split(" ",2);
        //modelAndView.addObject(ViewModelConstants.DETECT_OUT_IMAGE, detectOut);
        modelAndView.addObject(ViewModelConstants.DETECT_OUT_IMAGE_percent, result[0]);
        modelAndView.addObject(ViewModelConstants.DETECT_OUT_words, result[1]);

        // step 3. return detect result page
        return modelAndView;
    }

}