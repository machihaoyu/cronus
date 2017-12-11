package com.fjs.cronus.controller.csv;

/**
 * Created by yinzf on 2017/8/16.
 */

import com.csvreader.CsvReader;
import com.fjs.cronus.Common.CommonConst;
import com.fjs.cronus.Common.CommonMessage;
import com.fjs.cronus.api.thea.Loan;
import com.fjs.cronus.dto.CronusDto;
import com.fjs.cronus.dto.cronus.CustomerDTO;
import com.fjs.cronus.dto.uc.UserInfoDTO;
import com.fjs.cronus.model.PrdCustomer;
import com.fjs.cronus.service.CustomerInfoService;
import com.fjs.cronus.service.PrdCustomerService;
import com.fjs.cronus.service.uc.UcService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class UploadController {
    private Logger logger = LoggerFactory.getLogger(UploadController.class);

    @Autowired
    private CustomerInfoService customerInfoService;
    @Autowired
    private UcService thorUcService;
    @Autowired
    private PrdCustomerService prdCustomerService;

    @ApiOperation(value="导入公盘", notes="导入公盘")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "param", value = "上传文件参数（csv传1，excel传2）", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public CronusDto upload(HttpServletRequest request, HttpServletResponse response, MultipartFile file, Integer param)
            throws ServletException, IOException {
        CronusDto theaApiDTO=new CronusDto();
        try{
//            System.out.println("开始导入");
            String token=request.getHeader("Authorization");
//            ThorApiDTO<UserInfoDTO> thorApiDTO=thorUcService.getUserInfoByToken(token, CommonConst.SYSTEMNAME);
//            UserInfoDTO userInfoDTO=thorApiDTO.getData();
            String path = request.getSession().getServletContext().getRealPath("upload");
            String fileName = file.getOriginalFilename();
//            System.out.println(path);
            File targetFile = new File(path, fileName);
            if(!targetFile.exists()){
                targetFile.createNewFile();
            }
            if (param == null){
                theaApiDTO.setResult(CommonMessage.FAIL.getCode());
                theaApiDTO.setMessage(CommonConst.UNVALID_PARA);
                return theaApiDTO;
            }
            //保存
            //获取输出流
            OutputStream os=new FileOutputStream(targetFile);
            //获取输入流 CommonsMultipartFile 中可以直接得到文件的流
            InputStream is=file.getInputStream();
            int temp;
            //一个一个字节的读取并写入
            while((temp=is.read())!=(-1))
            {
                os.write(temp);
            }
            os.flush();
            os.close();
            is.close();

//            System.out.println("fileUrl:" + request.getContextPath()+"/upload/"+fileName);
            if (param == 1){
                readerCsv(targetFile.getPath(),token);
            }
            if (param == 2){
                Workbook wb=null;
                String ext = targetFile.getPath().substring(targetFile.getPath().lastIndexOf("."));
                InputStream is2 = new FileInputStream(targetFile.getPath());
                    if(".xls".equals(ext)){
                        wb = new HSSFWorkbook(is2);
                    }else if(".xlsx".equals(ext)){
                        wb = new XSSFWorkbook(is2);
                    }else{
                        wb=null;
                    }
                readExcelContent(targetFile.getPath(),wb,token);
            }

            deleteFile(targetFile.getPath());
            response.setCharacterEncoding("UTF-8");
            theaApiDTO.setResult(CommonMessage.SUCCESS.getCode());
            theaApiDTO.setMessage(CommonMessage.SUCCESS.getCodeDesc());

        }catch (Exception e){
            logger.error("导入公盘失败",e);
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc());
        }
        return theaApiDTO;
    }

    /**
     * 读取csv
     * @param csvFilePath
     * @throws Exception
     */
    @Transactional
    public void readerCsv(String csvFilePath,String token) throws Exception {

        CsvReader reader = new CsvReader(csvFilePath, ',',
                Charset.forName("GBK"));
        reader.readHeaders();
        String[] headers = reader.getHeaders();

        List<Object[]> list = new ArrayList<Object[]>();
        while (reader.readRecord()) {
            list.add(reader.getValues());
        }
        Object[][] datas = new String[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            datas[i] = list.get(i);
        }
        //输出
//        for (int i = 0; i < headers.length; i++) {
//            System.out.print(headers[i] + "\t");
//        }
//        System.out.println("");
        for (int i = 0; i < datas.length; i++) {
            Object[] data = datas[i]; // 取出一组数据
            CustomerDTO customerDTO=new CustomerDTO();
            for (int j = 0; j < data.length; j++) {
                Object cell = data[j];
                System.out.print(cell + "\t");
                if (j==0){
                    String customerName=cell.toString();
                    customerDTO.setCustomerName(customerName);
                }
                if (j==1){
                    String phone=cell.toString();
                    customerDTO.setTelephonenumber(phone);
                }
                if (j== 2){
                    String customerSource=cell.toString();
                    customerDTO.setCustomerSource(customerSource);
                }
                if (j== 3){
                    String utmSource=cell.toString();
                    customerDTO.setUtmSource(utmSource);
                }
                if (j== 4){
                    String mindAmount=cell.toString();
                    if (StringUtils.isNotEmpty(mindAmount)){
                        BigDecimal ma=new BigDecimal(mindAmount);
                        customerDTO.setLoanAmount(ma);
                    }
                }
                if (j== 5){
                    String city=cell.toString();
                    customerDTO.setCity(city);
                }
            }
//            System.out.println(loan.toString());
            if (customerDTO != null){
                logger.info("导入的csv:"+customerDTO.toString());
                UserInfoDTO userInfoDTO=thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
                //TODO 增加客户
                CronusDto cronusDto = customerInfoService.addCustomer(customerDTO,token);
            }
        }
        reader.close();
    }

    /**
     * 删除单个文件
     *
     * @param fileName
     *            要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("删除单个文件" + fileName + "成功！");
                return true;
            } else {
                System.out.println("删除单个文件" + fileName + "失败！");
                return false;
            }
        } else {
            System.out.println("删除单个文件失败：" + fileName + "不存在！");
            return false;
        }
    }
    //将Loan的值赋给客户
    public  CustomerDTO copyProperty(Loan loan){
        CustomerDTO customerDTO=new CustomerDTO();
        customerDTO.setCustomerName(loan.getCustomerName());
        customerDTO.setTelephonenumber(loan.getTelephonenumber());
        customerDTO.setCity(loan.getCity());
        return customerDTO;
    }

    @ApiOperation(value="文件下载", notes="文件下载")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Authorization", value = "认证信息", required = true, paramType = "header", defaultValue = "Bearer 467405f6-331c-4914-beb7-42027bf09a01", dataType = "string"),
            @ApiImplicitParam(name = "param", value = "下载文件参数（csv传1，excel传2）", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "file/download", method = RequestMethod.GET)
    public CronusDto fileDownload( HttpServletRequest request, HttpServletResponse response,Integer param){
        CronusDto theaApiDTO=new CronusDto();
        //获取网站部署路径(通过ServletContext对象)，用于确定下载文件位置，从而实现下载
        String path = request.getSession().getServletContext().getRealPath("download");
        //1.设置文件ContentType类型，这样设置，会自动判断下载文件类型
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("UTF-8");
        //2.设置文件头：最后一个参数是设置下载文件名(假如我们叫a.pdf)
        if (param == null){
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage(CommonConst.UNVALID_PARA);
            return theaApiDTO;
        }
        File file = null;
        String fileName=null;
        if (param == 1){
            file=new File(path + "\\"+"Template.xlsx");
            fileName="Template.xlsx";
        }
        if (param == 2){
            file=new File(path + "\\"+"导入客户到公盘模板.csv");
            fileName="导入客户到公盘模板.csv";
        }
        try{

            //2.设置文件头：最后一个参数是设置下载文件名
            response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes("utf-8"),"ISO-8859-1"));
            ServletOutputStream out;
            FileInputStream inputStream = new FileInputStream(file);
            //3.通过response获取ServletOutputStream对象(out)
            out = response.getOutputStream();
            int b = 0;
            byte[] buffer = new byte[1024];
            //创建缓冲区
            int len = 0;
            //循环将输入流中的内容读取到缓冲区当中
            while((len=inputStream.read(buffer))>0){
                //输出缓冲区的内容到浏览器，实现文件下载
                out.write(buffer, 0, len);
            }
            inputStream.close();
            out.close();
            out.flush();
            theaApiDTO.setResult(CommonMessage.SUCCESS.getCode());
            theaApiDTO.setMessage(CommonMessage.SUCCESS.getCodeDesc());

        } catch (IOException e) {
            logger.error("文件下载失败",e);
            theaApiDTO.setResult(CommonMessage.FAIL.getCode());
            theaApiDTO.setMessage(CommonMessage.FAIL.getCodeDesc());
        }
        return theaApiDTO;
    }

    /**
     * 读取Excel数据内容
     * @return
     * @throws Exception
     */
    public void readExcelContent(String filepath,Workbook wb,String token) throws Exception{
        if(wb==null){
            throw new Exception("Workbook对象为空！");
        }
        List<Object> content = new ArrayList<>();

        Sheet sheet = wb.getSheetAt(0);
        // 得到总行数
        int rowNum = sheet.getLastRowNum();
        Row row = sheet.getRow(0);
        int colNum = row.getPhysicalNumberOfCells();
        // 正文内容应该从第二行开始,第一行为表头的标题
        for (int i = 1; i <= rowNum; i++) {
            row = sheet.getRow(i);
            int j = 0;
            List<Object> cellValue = new ArrayList<>();
            while (j < colNum) {
                Object obj = getCellFormatValue(row.getCell(j));
                cellValue.add(obj);
                j++;
            }
            content.add(cellValue);
        }
        Object[] datas = new String[content.size()][];
        for (int i = 1; i < content.size(); i++) {
            List<Object> list = (List<Object>) content.get(i);
            Integer listSize=list.size();
            PrdCustomer prdCustomer=new PrdCustomer();
            for (int j = 0; j < listSize; j++) {
                if (j == 0){
                    String city=list.get(j).toString();
                    prdCustomer.setCity(city);
                }
                if (j ==1 ){
                    String customerSource=list.get(j).toString();
                    prdCustomer.setCustomerSource(customerSource);
                }
                if (j == 2){
                    String utmSource=list.get(j).toString();
                    prdCustomer.setUtmSource(utmSource);
                }
                if (j == 3){
                    String customerName=list.get(j).toString();
                    prdCustomer.setCustomerName(customerName);
                }
                if (j== 4){
                    String telephonenumber=list.get(j).toString();
                    prdCustomer.setTelephonenumber(telephonenumber);
                }
                if (j== 5){
                    String level=list.get(j).toString();
                    prdCustomer.setLevel(level);
                }
//                System.out.println(list.get(j));
            }
            logger.info("市场推广导入:"+prdCustomer.toString());
            UserInfoDTO userInfoDTO=thorUcService.getUserIdByToken(token, CommonConst.SYSTEMNAME);
            prdCustomerService.addPrdCustomer(prdCustomer,userInfoDTO);
        }
    }

    /**
     * 根据Cell类型设置数据
     * @param cell
     * @return
     */
    private Object getCellFormatValue(Cell cell) {
        Object cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_NUMERIC:// 如果当前Cell的Type为NUMERIC
                case Cell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式
                        // data格式是带时分秒的：2013-7-10 0:00:00
                        // cellvalue = cell.getDateCellValue().toLocaleString();
                        // data格式是不带带时分秒的：2013-7-10
                        Date date = cell.getDateCellValue();
                        cellvalue = date;
                    } else {// 如果是纯数字
                        // 取得当前Cell的数值
                        DecimalFormat decimalFormat = new DecimalFormat("#.#");
                        cellvalue = decimalFormat.format((cell.getNumericCellValue()));
                    }
                    break;
                }
                case Cell.CELL_TYPE_STRING:// 如果当前Cell的Type为STRING
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                default:// 默认的Cell值
                    cellvalue = "";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;
    }

    //将Loan的值赋给客户
    public  CustomerDTO copyProperty(PrdCustomer prdCustomer){
        CustomerDTO customerDTO=new CustomerDTO();
        customerDTO.setCity(prdCustomer.getCity());
        customerDTO.setCustomerName(prdCustomer.getCustomerName());
        customerDTO.setTelephonenumber(prdCustomer.getTelephonenumber());

        return customerDTO;
    }
}
