package igs.swnm.mq;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import igs.swnm.dao.SwnmDao;
import igs.swnm.utils.Constants;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;

public class SwcNewListener implements MessageListener {

	private RedisTemplate redisTemplate;

	@Autowired
	private SwnmDao swnmDao;

	public void setRedisTemplate(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void onMessage(Message msg, byte[] arg1) {
		byte[] body = msg.getBody();

		final Map<String, String> newSwc = (Map<String, String>) redisTemplate
				.getValueSerializer().deserialize(body);

		Executors.defaultThreadFactory().newThread(new Runnable() {
			public void run() {
				int count = Integer.parseInt(newSwc
						.get(Constants.Q_SWC_NEW_PROP_SWC_RCOUNT));
				String swc_id = newSwc.get(Constants.Q_SWC_NEW_PROP_SWC_HW_ID);
				swnmDao.insertNmSwcBaseBySwcId(swc_id);
				Map<String, String> m = new HashMap<String, String>();
				m.put("swc_id", swc_id);
				for (int i = 0; i < count; i++) {
					m.put("rel_id",
							String.format(Constants.Q_SWC_NEW_PROP_REL_ID, i));
					swnmDao.insertNmRelBaseByRelId(m);
				}
			}
		}).run();

	}

}
