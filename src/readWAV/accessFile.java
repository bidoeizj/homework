package readWAV;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;





public class accessFile {
	public static void main(String[] args) throws Exception {
        Data data = accessFile.access(new File("src/爱笑的眼睛_40dB.wav"));
        double [] AimDouble =data.getdouble(data.getPreData());
    }
	public static Data access(File tt) throws Exception{
		RandomAccessFile file = new RandomAccessFile(tt,"r");
	//检验是否是有效的音频文件
		if (file.length() < 46)
            throw new Exception("this is not a valid file");
	//检验是否是RIFF格式文件
		if(file.readInt()!=0x52494646)
			throw new Exception("this is not a RIFF file");
	//检验是否是PCM格式文件
		file.seek(20);
		if(file.readShort()!=0x0100)
			throw new Exception("this is not a PCM file");
	//检验是否是单声道音频
		file.seek(22);
		if(file.readShort()!=0x0100)
			throw new Exception("this is not a single track file");
	//检验采样率是否为44100Hz
		file.seek(24);
		if(file.readInt()!=0x44AC0000)
			throw new Exception("SampleRate is not 44100");
	//检验位深度是否为16bit
		file.seek(34);
		if(file.readShort()!=0x1000)
			throw new Exception("BitsPerSample is not 16bit");
		
	//将由小端法表示的数据节点长度翻译出来，存在size里
		file.seek(42);
		int a = file.readByte() & 0xFF;
		file.seek(43);
        int b = (file.readByte() << 8) & 0xFF00;
        file.seek(44);
        int c = (file.readByte() << 16) & 0xFF0000;
        file.seek(45);
        int d = (file.readByte() << 24) & 0xFF000000;
        int length = d | c | b | a;
    //将byte型数据存入byte型数组中
		Data data1 = new Data();
	    byte[] pre = new byte[length];
	    file.seek(46);
	    file.read(pre);
	    data1.setPreData(pre);
	    return data1;
		
	}
}
