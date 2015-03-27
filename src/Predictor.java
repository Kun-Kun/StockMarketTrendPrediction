import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;


public class Predictor {

	public static ArrayList<Day> data = new ArrayList<>();
	
	public static void writeARFF(String out, String rel) throws IOException {
		File file = new File(out);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write("@relation " + rel); bw.newLine();
		bw.write("@attribute PVI numeric"); bw.newLine();
		bw.write("@attribute NVI numeric"); bw.newLine();
		bw.write("@attribute OBV numeric"); bw.newLine();
		bw.write("@attribute TV numeric"); bw.newLine();
		bw.write("@attribute PVT numeric"); bw.newLine();
		bw.write("@attribute ADO numeric"); bw.newLine();
		bw.write("@attribute CO numeric"); bw.newLine();
		bw.write("@attribute CV numeric"); bw.newLine();
		bw.write("@attribute A numeric"); bw.newLine();
		bw.write("@attribute HH numeric"); bw.newLine();
		bw.write("@attribute LL numeric"); bw.newLine();
		bw.write("@attribute RSI numeric"); bw.newLine();
		bw.write("@attribute NPMA numeric"); bw.newLine();
		bw.write("@attribute MACDL numeric"); bw.newLine();
		bw.write("@attribute M numeric"); bw.newLine();
		bw.write("@attribute SOK numeric"); bw.newLine();
		bw.write("@attribute SOD numeric"); bw.newLine();
		bw.write("@attribute TP numeric"); bw.newLine();
		bw.write("@attribute MP numeric"); bw.newLine();
		bw.write("@attribute WC numeric"); bw.newLine();
		bw.write("@attribute WR numeric"); bw.newLine();
		bw.write("@attribute PRC numeric"); bw.newLine();
		bw.write("@attribute WAD numeric"); bw.newLine();
		bw.write("@attribute BUB numeric"); bw.newLine();
		bw.write("@attribute BMB numeric"); bw.newLine();
		bw.write("@attribute BLB numeric"); bw.newLine();
		bw.write("@attribute TFMA numeric"); bw.newLine();
		bw.write("@attribute SFMA numeric"); bw.newLine();
		bw.write("@attribute Trend {up,down,none}"); bw.newLine();
		bw.write("@data"); bw.newLine();
		for(int i = 0; i < data.size(); i++) {
			bw.write(data.get(i).pvi + "," + data.get(i).nvi + "," + data.get(i).obv + "," + data.get(i).tv + "," + data.get(i).pvt + "," + data.get(i).ado + "," + data.get(i).co + "," + data.get(i).cv + "," + data.get(i).a + "," + data.get(i).hh + "," + data.get(i).ll + "," + data.get(i).rsi + "," + data.get(i).npma + "," + data.get(i).macdl + "," + data.get(i).m + "," + data.get(i).sok + "," + data.get(i).sod + "," + data.get(i).tp + "," + data.get(i).mp + "," + data.get(i).wc + "," + data.get(i).wr + "," + data.get(i).prc + "," + data.get(i).wad + "," + data.get(i).bub + "," + data.get(i).bmb + "," + data.get(i).blb + "," + data.get(i).tfma + "," + data.get(i).sfma + "," + data.get(i).trend); bw.newLine();
		}
		bw.close();
	}
	
	public static void calculateTechnicalIndices()
	{
		double pvi, nvi, obv, tv, pvt, ado, co, cv, a, hh = 0, ll = data.get(0).low, rsi, npma, macdl, m, sok, sod, tp, mp, wc, wr, proc, wad, bub, bmb, blb, tfma, sfma, volumeChange;
		double alpha = 0.5;
		double ema[] = new double[data.size()];
		for(int i = 0; i < data.size(); i++) {
			if(i == 0) {
				ema[i] = data.get(i).close;
				
				data.get(i).pvi = 1;
				data.get(i).nvi = 1;
				data.get(i).obv = data.get(i).volume;
				data.get(i).tv = data.get(i).volume;
				data.get(i).pvt = 0;
				data.get(i).ado = 0;
				data.get(i).co = 0;
				data.get(i).hla = 0;
				data.get(i).cv = 0;
				data.get(i).a = 0;
				data.get(i).hh = data.get(i).high;
				data.get(i).ll = data.get(i).low;
				data.get(i).rsi = 0;
				data.get(i).npma = 0;
				data.get(i).macdl = 0;
				data.get(i).m = 0;
				data.get(i).sok = 0;
				data.get(i).sod = 0;
				data.get(i).tp = (data.get(i).high + data.get(i).low + data.get(i).close) / 3;
				data.get(i).mp = (data.get(i).high + data.get(i).low) / 2;
				data.get(i).wc = ((data.get(i).close * 2) + data.get(i).high + data.get(i).low) / 4;
				data.get(i).wr = 0;
				data.get(i).prc = 0;
				data.get(i).wad = 0;
				data.get(i).bub = 0;
				data.get(i).bmb = 0;
				data.get(i).blb = 0;
				data.get(i).tfma = 0;
				data.get(i).sfma = 0;
				data.get(i).trend = "none";
			}
			else {			
				ema[i] = (alpha * data.get(i).close) + ((1-alpha) * ema[i-1]);
				
				//PVI
				volumeChange = data.get(i).volume - data.get(i-1).volume; 
				if(volumeChange > 0) {
					pvi = data.get(i-1).pvi + (((data.get(i).close - data.get(i-1).close) / data.get(i-1).close) * data.get(i-1).pvi);
				}
				else {
					pvi = data.get(i-1).pvi;
				}
				data.get(i).pvi = pvi;

				//NVI
				if(volumeChange < 0) {
					nvi = data.get(i-1).nvi + (((data.get(i).close - data.get(i-1).close) / data.get(i-1).close) * data.get(i-1).nvi);
				}
				else {
					nvi = data.get(i-1).nvi;
				}
				data.get(i).nvi = nvi;
				
				//OBV
				if(data.get(i).close > data.get(i-1).close) {
					obv = data.get(i).obv + data.get(i).volume;
					data.get(i).obv = obv;
				}
				else if(data.get(i).close < data.get(i-1).close) {
					obv = data.get(i).obv - data.get(i).volume;
					data.get(i).obv = obv;
				}
				
				//TV/ADTV
				double sum = 0, count = 0;
				for(int j = i; j > 0; j--) {
					sum = sum + data.get(j).volume;
					count++;
				}
				data.get(i).tv = sum/count;				
				
				//PVT/VPT
				data.get(i).pvt = data.get(i-1).pvt + (data.get(i).volume * ((data.get(i).close - data.get(i-1).close) / data.get(i-1).close));
				
				//ADO
				data.get(i).ado = data.get(i-1).ado + (data.get(i).volume * ((data.get(i).close - data.get(i).low) - (data.get(i).high - data.get(i).close)) / (data.get(i).high - data.get(i).low));
				
				//CO
				if(i > 10) {
					double tenema = data.get(i-10).ado, threeema = data.get(i-3).ado, coalpha = 0.5;
					for(int j = i-9; j <= i; j++) {
						tenema = (coalpha * data.get(j).ado) + ((1 - coalpha) * tenema);
					}
					for(int j = i-2; j <= i; j++) {
						threeema = (coalpha * data.get(j).ado) + ((1 - coalpha) * threeema);
					}
					data.get(i).co = threeema - tenema;
				}
				else {
					data.get(i).co = 0.0;
				}
				
				//CV
				if(i > 10) {
					double hla = data.get(i-10).high - data.get(i-10).low, cvalpha = 0.5;
					for(int j = i-9; j <= i; j++) {
						hla = (cvalpha * (data.get(j).high - data.get(j).low)) + ((1 - cvalpha) * hla);
					}
					data.get(i).hla = hla;
				}
				else {
					data.get(i).hla = 0.0;
				}
				
				if(i > 20) {
					data.get(i).cv = ((data.get(i).hla - data.get(i-10).hla) / data.get(i-10).hla) * 100.0;
				}
				else {
					data.get(i).cv = 0.0;
				}
				
				//A
				data.get(i).a = (data.get(i).close - data.get(0).close) / (i-1);
				
				//HH
				if(data.get(i).high > hh) {
					hh = data.get(i).high;
				}
				else {
					hh = data.get(i-1).hh;
				}
				data.get(i).hh = hh;
				
				//LL
				if(data.get(i).low < ll) {
					ll = data.get(i).low;
				}
				else {
					ll = data.get(i-1).ll;
				}
				data.get(i).ll = ll;
				
				//RSI
				double upsum = 0, upcount = 0, downsum = 0, downcount = 0;
				for(int j = i; j > 0; j--) {
					if(data.get(i).close >= data.get(i-1).close) {
						upsum = upsum + data.get(i).close;
						upcount++;
					}
					else {
						downsum = downsum + data.get(i).close;
						downcount++;
					}
				}
				data.get(i).rsi = 100.0 - (100.0/(1.0 + (upsum/upcount)/(downsum+downcount)));
				
				//NPMA
				double npmasum = 0, npmacount = 0;
				if(i > 9) {
					for(int j = i; j > i-9; j--) {
						npmasum = npmasum + ema[j];
						npmacount++;
					}
					data.get(i).npma = npmasum/npmacount;
				}
				else {
					data.get(i).npma = 0.0;
				}
				
				//MACDL
				double twelvesum = 0, twelvecount = 0, twentysixsum = 0, twentysixcount = 0;
				if(i > 26) {
					for(int j = i; j > i-26; j--) {
						twentysixsum = twentysixsum + ema[j];
						twentysixcount++;
					}
					for(int j = i; j > i-12; j--) {
						twelvesum = twelvesum + ema[j];
						twelvecount++;
					}
					data.get(i).macdl = (twelvesum/twelvecount) - (twentysixsum/twentysixcount);
				}
				else {
					data.get(i).macdl = 0.0;
				}
				
				//M
				data.get(i).m = data.get(i).close - data.get(0).close;
				
				//SOK
				data.get(i).sok = 100 * ((data.get(i).close - data.get(i).ll) / (data.get(i).hh - data.get(i).ll));
				
				//SOD
				double sodsum = 0, sodcount = 0;
				if(i > 3) {
					for(int j = i; j > i-3; j--) {
						sodsum = sodsum + data.get(j).sok;
						sodcount++;
					}
					data.get(i).sod = sodsum / sodcount;
				}
				else {
					data.get(i).sod = 0;
				}
				
				//TP
				data.get(i).tp = (data.get(i).high + data.get(i).low + data.get(i).close) / 3.0;
				
				//MP
				data.get(i).mp = (data.get(i).hh + data.get(i).ll) / 2.0;
				
				//WC
				data.get(i).wc = ((data.get(i).close * 2.0) + data.get(i).high + data.get(i).low) / 4.0;
				
				//WR
				data.get(i).wr = ((data.get(i).hh - data.get(i).close) / (data.get(i).hh - data.get(i).ll)) * -100.0;
				
				//PRC
				data.get(i).prc = data.get(i).close - data.get(0).close;
				
				//WAD
				double trh, trl, tad;
				if(data.get(i).high >= data.get(i-1).close) {
					trh = data.get(i).high;
				}
				else {
					trh = data.get(i-1).close;
				}
				
				if(data.get(i).low <= data.get(i-1).close) {
					trl = data.get(i).low;
				}
				else {
					trl = data.get(i-1).close;
				}
				
				if(data.get(i).close > data.get(i-1).close) {
					tad = data.get(i).close - trl;
				}
				else if(data.get(i).close < data.get(i-1).close) {
					tad = data.get(i).close - trh;
				}
				else {
					tad = 0;
				}
				
				data.get(i).wad = tad + data.get(i-1).wad;
				
				//BMB
				if(i > 20) {
					double bmbsum = 0, bmbcount = 0;
					for(int j = i; j > i-20; j--) {
						bmbsum = bmbsum + data.get(j).close;
						bmbcount++;
					}
					data.get(i).bmb = bmbsum / bmbcount;
				}
				else {
					data.get(i).bmb = 0.0;
				}
				
 				//BUB
				if(i > 20) {
					double bubsum = 0, bubcount = 0;
					for(int j = i; j > i-20; j--) {
						bubsum = bubsum + Math.pow(data.get(j).close - data.get(j).bmb, 2);
						bubcount++;
					}
					data.get(i).bub = data.get(i).bmb + (2 + Math.sqrt(bubsum / bubcount));
				}
				else {
					data.get(i).bub = 0.0;
				}
								
				//BLB
				if(i > 20) {
					double blbsum = 0, blbcount = 0;
					for(int j = i; j > i-20; j--) {
						blbsum = blbsum + Math.pow(data.get(j).close - data.get(j).bmb, 2);
						blbcount++;
					}
					data.get(i).blb = data.get(i).bmb - (2 + Math.sqrt(blbsum / blbcount));
				}
				else {
					data.get(i).blb = 0.0;
				}
				
				//TFMA
				if(i > 25) {
					double temp = 0;
					for(int j = i; j>= i-25; j--) {
						temp = temp + data.get(j).close;
					}
					tfma = temp/25.0;
					data.get(i).tfma = tfma;
				}
				else {
					data.get(i).tfma = 0.0;
				}
				
				//SFMA
				if(i > 65) {
					double temp = 0;
					for(int j = i; j>= i-65; j--) {
						temp = temp + data.get(j).close;
					}
					sfma = temp/65.0;
					data.get(i).sfma = sfma;
				}
				else {
					data.get(i).sfma = 0.0;
				}
				
				//Trend
				String trend;
				if((data.get(i).close > data.get(i).tfma) && (data.get(i).tfma > data.get(i).sfma) && (data.get(i).tfma > data.get(i-1).tfma) && (data.get(i-1).tfma > data.get(i-2).tfma) && (data.get(i-2).tfma > data.get(i-3).tfma) && (data.get(i-3).tfma > data.get(i-4).tfma) && (data.get(i-4).tfma > data.get(i-5).tfma) && (data.get(i).sfma > data.get(i-1).sfma)) {
					trend = "up";
				}
				else if((data.get(i).close < data.get(i).tfma) && (data.get(i).tfma < data.get(i).sfma) && (data.get(i).tfma < data.get(i-1).tfma) && (data.get(i-1).tfma < data.get(i-2).tfma) && (data.get(i-2).tfma < data.get(i-3).tfma) && (data.get(i-3).tfma < data.get(i-4).tfma) && (data.get(i-4).tfma < data.get(i-5).tfma) && (data.get(i).sfma < data.get(i-1).sfma)) {
					trend = "down";
				}
				else {
					trend = "none";
				}
				data.get(i).trend = trend;
			}
		}
	}
	
	public static void populateData(String in) throws FileNotFoundException {
		Scanner scan = new Scanner(new File(in));
		String s = new String();
		String[] s2;
		int i = 0;
		while(scan.hasNext()) {
			s = scan.nextLine();
			s2 = s.split(",");
			if(i != 0) {
				double open = Double.parseDouble(s2[1]);
				double close = Double.parseDouble(s2[4]);
				double high = Double.parseDouble(s2[2]);
				double low = Double.parseDouble(s2[3]);
				double volume = Double.parseDouble(s2[5]);
				data.add(new Day(open, close, high, low, volume));
			}
			i++;
		}
		scan.close();
	}
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		populateData(args[0]);
		System.out.println(data.size() + " entries added.");
		calculateTechnicalIndices();
		writeARFF(args[1], args[2]);
	}

}
