package shazam.hash;
import java.util.ArrayList;
/**
 * Created by 阿狸 on 2016/11/26.
 */
public class FingerPrint {
    public static final double scaling= FFT.WINDOW_SIZE/44100.0;
    public static final int N=3;

    private ArrayList<int[]> constel_data=new ArrayList<>();
    private int id;
    public FingerPrint(int id){
        this.id=id;
    }
    public FingerPrint(){
        this.id=-1;
    }
    //划分
    public int partition(double[]a,int lo,int hi,int[]Tindex){
        int i=lo-1;
        for(int j=lo;j<hi;j++){
            if(a[j]>a[hi]){
                //交换数组元素
                double temp=a[++i];
                a[i]=a[j];
                a[j]=temp;
                //交换索引
                int Itemp=Tindex[i];
                Tindex[i]=Tindex[j];
                Tindex[j]=Itemp;
            }
        }
        double temp2=a[++i];
        a[i]=a[hi];
        a[hi]=temp2;
        int Ttemp2=Tindex[i];
        Tindex[i]=Tindex[hi];
        Tindex[hi]=Ttemp2;
        return i;
    }
    //QuickSelect
    //选出数组中第p大的元素的索引
    public int select(double[]a,int lo,int hi,int p,int []Tindex){
        if(lo==hi)return lo;
        int q=partition(a,lo,hi,Tindex);
        int k=q-lo+1;
        if(p==k) return Tindex[k-1];
        if(p<k) return select(a,lo,q-1,p,Tindex);
        else return select(a,q+1,hi,p-k,Tindex);
    }
    public void append(double[]freDomain){
        int[]freqPeaks=new int[N];
        int M=freDomain.length;
        int []index=new int[M];//储存频率索引
        double[]ret=FFT.fft(freDomain);//存储频率的振幅
        for(int i=0;i<M;i++){
            index[i]=i;
        }
        for(int i=0;i<N;i++){
            freqPeaks[i]=select(ret,5,500,i+1,index);
        }
        constel_data.add(freqPeaks);
    }
    public ArrayList<Hash>combineHash(){
        if(constel_data.size()<3)
            throw new RuntimeException("Too few frequency peaks");
        ArrayList<Hash>hashes=new ArrayList<>();
        for(int i=0;i<constel_data.size()-2;++i){
            for(int k=0;k<N;++k){
                for(int j=1;j<=2;++j){
                    for(int kk=1;kk<N;++kk){
                        Hash hash=new Hash();
                        hash.f1=(short)constel_data.get(i)[k];
                        hash.f2=(short)constel_data.get(i+j)[kk];
                        hash.dt=(short)j;
                        hash.song_id=id;
                        hash.offset=i;
                        hashes.add(hash);
                    }
                }
            }
        }
        return hashes;
    }
}
