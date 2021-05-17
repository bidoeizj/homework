package readWAV;

public class Data {
	 private byte[] pre;
	    private int double_length;

	    byte[] getPreData() {
	        return pre;
	    }
	//存入byte型数组，并且将其转化为double型，存入double_中
	    void setPreData(byte[] preData) {
	        if (preData.length % 2 != 0)
	            throw new RuntimeException("the number of bytes is not even");
	        this.pre = preData;
	        double_length = preData.length / 2;
	    }

	//把byte型数据直接翻译为double型数据
	    double [] getdouble(byte[] preData){
	    	double [] temp= new double [double_length];
	    	for(int i =0;i<double_length;i++){
	    		int a = (preData[2*i] & 0xFF);		//记录最低位
	    		int b = (preData[2*i+1] & 0xFF);	//记录最高位
	    		b <<=8;
	    		double s = (b|a)/32768.0;			//直接转化为double型
	    		temp[i]=s;
	    	}
			return temp;
	    	
	    }

	   

	    public int getSampleNum() {
	        return double_length;
	    }
	}