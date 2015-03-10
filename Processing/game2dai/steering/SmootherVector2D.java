//package game2dai.steering;
//
//import game2dai.maths.Vector2D;
//
//
//public class SmootherVector2D {
//
//	Vector2D[] m_History;
//	int m_iNextUpdateSlot;
//
//	//an example of the 'zero' value of the type to be smoothed. This
//	//would be something like Vector2D(0,0)
//	Vector2D m_ZeroValue;
//
//	//to instantiate a Smoother pass it the number of samples you want
//	//to use in the smoothing, and an exampe of a 'zero' type
//	SmootherVector2D(int SampleSize){
//		m_iNextUpdateSlot = 0;
//		m_History = new Vector2D[SampleSize];
//		for(int i = 0; i < SampleSize; i++){
//			m_History[i] = new Vector2D();
//		}
//	}
//
//	//each time you want to get a new average, feed it the most recent value
//	//and this method will return an average over the last SampleSize updates
//	public Vector2D Update(Vector2D MostRecentValue)
//	{  
//		//overwrite the oldest value with the newest
//		m_History[m_iNextUpdateSlot++] = MostRecentValue;
//
//		//make sure m_iNextUpdateSlot wraps around.
//		m_iNextUpdateSlot %= m_History.length;
//		//	    if (m_iNextUpdateSlot == m_History.size()) m_iNextUpdateSlot = 0;
//
//		//now to calculate the average of the history list
//		Vector2D sum = m_ZeroValue;
//
//		//	    std::vector<T>::iterator it = m_History.begin();
//
//		for (int i = 0; i < m_History.length; ++i){
//			sum.add(m_History[i]);
//		}
//		sum.div((float)m_History.length);
//		return sum;
//	}
//
//
//}
