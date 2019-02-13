package com.yzlee.sui.server;

/**
 * @Author: yzlee
 * @Date: 2019/2/13 12:10
 */
public class App {

    public static void main(String[] args){
        Server.newInstance().start();
    }

/*	public static void main(String[] args) throws IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, InstantiationException {
		ServerSocket ss=new ServerSocket(12345);
		Socket s=ss.accept();

		System.out.println("接收到信息");

		BufferedReader br=new BufferedReader(new InputStreamReader(s.getInputStream()));
		String json=br.readLine();

		Gson gson=new Gson();
		ProtocolEntity entity=gson.fromJson(json, ProtocolEntity.class);
		Method[] ms=A.class.getDeclaredMethods();
		System.out.println("-----------");
		for(Method m:ms){
			if(!m.getName().equals(entity.getMethodName())){
				continue;
			}
			Class<?>[] cs=m.getParameterTypes();
			if(cs.length!=entity.getParamsType().size()){
				continue;
			}
			boolean flag=true;
			for(int i=0;i<cs.length;i++){
				System.out.println(cs[i].getCanonicalName()+"===="+entity.getParamsType().get(i));
				if(!cs[i].getTypeName().equals(entity.getParamsType().get(i))){
					flag=false;
					break;
				}
			}
			System.out.println("-----------");
			if(!flag){
				continue;
			}
			System.out.println("-----------");
			//转换参数
			Object[] o=new Object[entity.getParams().size()];
			for(int i=0;i<entity.getParams().size();i++){
				try {
					byte[] bytes=Base64.decode(entity.getParams().get(i));
					o[i]= CommonUtils.byteArraytoObject(bytes);
				} catch (Base64DecodingException e) {

					e.printStackTrace();
				}
			}
//			entity.getParams()[0]=new My(1342);
			Object ooo=m.invoke(Impl.class.newInstance(),o);
			System.out.println("返回值"+ooo);

		}

	}
	*/

}
