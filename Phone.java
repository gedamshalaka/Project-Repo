import java.util.*;
import java.io.*;

class Detail_Node{
	String FirstName;
	String LastName;
	String Address;
	int[] phone=new int[10];
	Detail_Node(String fname,String lname,String addr,int[] ph){
		this.FirstName=fname;
		this.LastName=lname;
		this.Address=addr;
		for(int i=0;i<10;i++){
			this.phone[i]=ph[i];
		}
	}
}
class Suffix_Node{
	String word;
	List<Suffix_Node> children;
	List<Detail_Node> LeafNode;
	Suffix_Node(){
		this.word=null;
		this.children=new ArrayList<Suffix_Node>();
		this.LeafNode=new ArrayList<Detail_Node>();
	}
}
class Trie_Node{
	Trie_Node[] children=new Trie_Node[10];
	boolean isLeaf;
	Detail_Node LeafNode;
	Trie_Node(){
		isLeaf=false;
		LeafNode=null;
		for(int i=0;i<10;i++){
			children[i]=null;
		}
	}
};
public class Phone{
	static Trie_Node phone_root=new Trie_Node();
	static Suffix_Node fname_root=new Suffix_Node();
	static Suffix_Node lname_root=new Suffix_Node();
	static Suffix_Node aname_root=new Suffix_Node();
	static void print_stree(Suffix_Node sn,int lvl){
		if(sn!=null){
			System.out.println(sn.word+" "+lvl);
			for(Suffix_Node s:sn.children){
				print_stree(s,lvl+1);
			}
		}
	}
	static void insert_stree(Suffix_Node root,Detail_Node dnode,String key){
		while(key.length()>0){
			Suffix_Node tmp=null;
			for(Suffix_Node dn:root.children){
				if((dn.word).charAt(0)==key.charAt(0)){
					tmp=dn;
					break;
				}
			}
			if(tmp==null){
				tmp=new Suffix_Node();
				(tmp.LeafNode).add(dnode);
				tmp.word=key;
				(root.children).add(tmp);
				key="";
			}
			else{
				if(key.equals(tmp.word)){
					(tmp.LeafNode).add(dnode);
					key="";
				}
				else if(key.startsWith(tmp.word)){
					String str=tmp.word;
					String nwrd=key.substring(str.length());
					key=nwrd;
					root=tmp;
				}
				else if((tmp.word).startsWith(key)){
					String str=tmp.word;
					String nwrd=str.substring(key.length());
					Suffix_Node sn=new Suffix_Node();
					tmp.word=key;
					for(Suffix_Node tsn:tmp.children){
						(sn.children).add(tsn);
					}
					//sn.children=tmp.children;
					(tmp.children).clear();
					for(Detail_Node tdn:tmp.LeafNode){
						(sn.LeafNode).add(tdn);
					}
					//sn.LeafNode=tmp.LeafNode;
					(tmp.LeafNode).clear();
					sn.word=nwrd;
					(tmp.LeafNode).add(dnode);
					(tmp.children).add(sn);
					key="";
				}
				else{
					int l1,l2;
					String str=tmp.word;
					l1=str.length();
					l2=key.length();
					int i=0;
					String nwrd;
					while(i<l1&&i<l2&&str.charAt(i)==key.charAt(i)){
						i++;
					}
					nwrd=key.substring(0,i);
					str=str.substring(i);
					key=key.substring(i);
					Suffix_Node s1,s2;
					s1=new Suffix_Node();
					s2=new Suffix_Node();
					for(Suffix_Node sn:tmp.children){
						(s1.children).add(sn);
					}
					for(Detail_Node sn:tmp.LeafNode){
						(s1.LeafNode).add(sn);
					}
					(tmp.children).clear();
					(tmp.LeafNode).clear();
					tmp.word=nwrd;
					s1.word=str;
					s2.word=key;
					(s2.LeafNode).add(dnode);
					(tmp.children).add(s1);
					(tmp.children).add(s2);
					key="";
				}
			}
		}
	}
	static ArrayList<Detail_Node> getAll(Suffix_Node root,ArrayList<Detail_Node> arr){
		if(root!=null){
			Set<Detail_Node> st=new HashSet<Detail_Node>();
			for(Detail_Node dn:arr){
				st.add(dn);
			}
			ArrayList<Detail_Node> ans=new ArrayList<Detail_Node>();
			for(Detail_Node dn:root.LeafNode){
				st.add(dn);
			}
			for(Detail_Node dn:st){
				ans.add(dn);
			}
			for(Suffix_Node sn:root.children){
				ans=getAll(sn,ans);
			}
			return ans;
		}
		else{
			return arr;
		}
	}
	static ArrayList<Detail_Node> search_stree(Suffix_Node root,ArrayList<Detail_Node> arr,String key,int pos){
		if(root==null||pos>=key.length()){
			return arr;
		}
		else{
			Suffix_Node tmp=null;
			for(Suffix_Node sn:root.children){
				if((sn.word).charAt(0)==key.charAt(pos)){
					tmp=sn;
					break;
				}
			}
			if(tmp==null){
				return arr;
			}
			else{
				String str=key.substring(pos);
				String wrd=tmp.word;
				if(str.equals(wrd)||wrd.startsWith(str)){
					arr=getAll(tmp,arr);
				}
				else if(str.startsWith(wrd)){
					arr=search_stree(tmp,arr,key,pos+wrd.length());
				}
				else{
					return arr;
				}
			}
		}
		return arr;
	}
	static boolean insert_fname(Detail_Node dnode){
		try{
			String str=dnode.FirstName;
			str=str.toLowerCase();
			int n=str.length();
			for(int i=0;i<n;i++){
				String subs=str.substring(i);
				insert_stree(fname_root,dnode,subs);
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	static boolean insert_lname(Detail_Node dnode){
		try{
			String str=dnode.LastName;
			str=str.toLowerCase();
			int n=str.length();
			for(int i=0;i<n;i++){
				String subs=str.substring(i);
				insert_stree(lname_root,dnode,subs);
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	static boolean insert_aname(Detail_Node dnode){
		try{
			String str=dnode.Address;
			str=str.replaceAll("\\s", "");
			str=str.toLowerCase();
			int n=str.length();
			for(int i=0;i<n;i++){
				String subs=str.substring(i);
				insert_stree(aname_root,dnode,subs);
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	static ArrayList<Detail_Node> search_fname(String key){
		ArrayList<Detail_Node> ret=new ArrayList<Detail_Node>();
		ret=search_stree(fname_root,ret,key,0);
		return ret;
	}
	static ArrayList<Detail_Node> search_lname(String key){
		ArrayList<Detail_Node> ret=new ArrayList<Detail_Node>();
		ret=search_stree(lname_root,ret,key,0);
		return ret;
	}
	static ArrayList<Detail_Node> search_aname(String key){
		ArrayList<Detail_Node> ret=new ArrayList<Detail_Node>();
		ret=search_stree(aname_root,ret,key,0);
		return ret;
	}
	static boolean insert_phone(String fname,String lname,String addr,int[] ph){
		try{
			Detail_Node dets=new Detail_Node(fname,lname,addr,ph);
			Trie_Node tmp=phone_root;
			for(int i=0;i<10;i++){
				if(tmp.children[ph[i]]==null){
					tmp.children[ph[i]]=new Trie_Node();
				}
				tmp=tmp.children[ph[i]];
			}
			tmp.isLeaf=true;
			if(tmp.LeafNode==null){
				tmp.LeafNode=dets;
				insert_fname(dets);
				insert_lname(dets);
				insert_aname(dets);
				return true;
			}
			else{
				System.out.println("Already exists");
				return false;
			}
		}
		catch(Exception e){
			return false;
		}
	}
	static Detail_Node search_phone(String kw){
		Trie_Node tmp=phone_root;
		int found=1;
		for(int i=0;i<10&&found==1;i++){
			char ch=kw.charAt(i);
			if(tmp.children[ch-'0']!=null){
				tmp=tmp.children[ch-'0'];
			}
			else{
				found=0;
			}
		}
		if(found==0){
			return null;
		}
		else{
			return tmp.LeafNode;
		}
	}
	static int rec_del(Suffix_Node root,Detail_Node dnode,String key,int posn){
		Suffix_Node tmp=null;
		for(Suffix_Node sn:root.children){
			if((sn.word).charAt(0)==key.charAt(posn)){
				tmp=sn;
			}
		}
		if(tmp!=null){
			String wrd=tmp.word;
			int i=posn;int j=0;
			while(j<wrd.length()&&i<key.length()&&wrd.charAt(j)==key.charAt(i)){
				i++;j++;
			}
			if(i==key.length()){
				int flag=0;
				List<Detail_Node> tmparr=new ArrayList<Detail_Node>();
				for(Detail_Node dn:tmp.LeafNode){
					if(dn!=dnode){
						flag=1;
						tmparr.add(dn);
					}
				}
				if(flag==1){
					tmp.LeafNode=tmparr;
					return 0;
				}
				else{
					tmp.word=null;
					return 1;
				}
			}
			else{
				int r=rec_del(tmp,dnode,key,i);
				if(r==1){
					List<Suffix_Node> tmp_arr=new ArrayList<Suffix_Node>();
					for(Suffix_Node sn:tmp.children){
						if(sn.word!=null){
							(tmp_arr).add(sn);
						}
					}
					tmp.children=tmp_arr;
					if((tmp.children).size()==0){
						tmp.word=null;
					}
					else{
						r=0;
					}
				}
				return r;
			}
		}
		return 0;
	}
	static void delete_stree(Suffix_Node root,Detail_Node dnode,String key){
		for(int i=0;i<key.length();i++){
			int r=rec_del(root,dnode,key,i);
			List<Suffix_Node> tmp_arr=new ArrayList<Suffix_Node>();
			for(Suffix_Node sn:root.children){
				if(sn.word!=null){
					tmp_arr.add(sn);
				}
			}
			(root.children).clear();
			root.children=tmp_arr;
		}
	}
	static void delete_fname(Detail_Node dn){
		String key=dn.FirstName;
		key=key.toLowerCase();
		delete_stree(fname_root,dn,key);
	}
	static void delete_lname(Detail_Node dn){
		String key=dn.LastName;
		key=key.toLowerCase();
		delete_stree(lname_root,dn,key);
	}
	static void delete_aname(Detail_Node dn){
		String key=dn.Address;
		key=key.replaceAll("\\s", "");
		key=key.toLowerCase();
		delete_stree(aname_root,dn,key);
	}
	static Trie_Node delete_phone(Trie_Node node,String s,int posn){
		if(node==null){
			throw new IllegalArgumentException();
		}
		else{
			if(posn==10){
				if(node==null){
					throw new IllegalArgumentException();
				}
				else{
					node.isLeaf=false;
					if(node.LeafNode!=null){
						delete_fname(node.LeafNode);
						delete_lname(node.LeafNode);
						delete_aname(node.LeafNode);
						node.LeafNode=null;
					}
					node=null;
					return null;
				}
			}
			else{
				node.children[s.charAt(posn)-'0']=delete_phone(node.children[s.charAt(posn)-'0'],s,posn+1);
				int flag=0;
				for(int i=0;i<10&&flag==0;i++){
					if(node.children[i]!=null){
						flag=1;
					}
				}
				if(flag==0){
					node=null;
				}
				return node;
			}
		}
	}
	public static void main(String[] args){
		try{
			File file=new File("input.json");
			Scanner scn=new Scanner(file);
			while(scn.hasNextLine()){
				String s1=scn.nextLine();
				String s2=s1.trim();
				s1=scn.nextLine();
				s2=s1.trim();
				String[] sarr=s2.split(":");
				String[] sarr1=sarr[1].split("\"");
				String operation=sarr1[1].trim();
				switch(operation){
					case "insert":	s1=scn.nextLine();
							s2=s1.trim();
							String fname,lname,addr,phstr;
							s1=scn.nextLine();
							s2=s1.trim();
							sarr=s2.split("\"");
							fname=sarr[3];
							s1=scn.nextLine();
							s2=s1.trim();
							sarr=s2.split("\"");
							lname=sarr[3];
							s1=scn.nextLine();
							s2=s1.trim();
							sarr=s2.split("\"");
							addr=sarr[3];
							s1=scn.nextLine();
							s2=s1.trim();
							sarr=s2.split("\"");
							phstr=sarr[3];
							int[] ph=new int[10];
							for(int i=0;i<10;i++){
								char ch=phstr.charAt(i);
								ph[i]=ch-'0';
							}
							boolean status=insert_phone(fname,lname,addr,ph);
							System.out.println("Insert status: "+status);
							s1=scn.nextLine();
							s2=s1.trim();
							s1=scn.nextLine();
							s2=s1.trim();
							break;
					case "search":	s1=scn.nextLine();
							s2=s1.trim();
							sarr=s2.split("\"");
							String field=sarr[3];
							s1=scn.nextLine();
							s2=s1.trim();
							sarr=s2.split("\"");
							String keyword=sarr[3];
							String keyword1=keyword.toLowerCase();
							switch(field){
								case "phone":	Detail_Node tn=null;
										tn=search_phone(keyword);
										if(tn==null){
											System.out.println("Search status: false");
										}
										else{
											System.out.println("Search status: true");
											System.out.print("{\n\t\"firstname\": "+tn.FirstName+"\n\t\"lastname\": "+tn.LastName+"\n\t\"address\": "+tn.Address+"\n\t\"phone\": ");
											for(int i=0;i<10;i++){
												System.out.print(tn.phone[i]);
											}
											System.out.print("\n}\n");
										}
										break;
								case "firstname":
										ArrayList<Detail_Node> ret=search_fname(keyword1);
										if(ret.size()>0){
											System.out.println("Search Status for firstname: "+keyword+" -Results Found");
											for(Detail_Node dn:ret){
												if(dn==null){
													System.out.println("null");
												}
												else{
													System.out.print("{\n\t\"firstname\": "+dn.FirstName+"\n\t\"lastname\": "+dn.LastName+"\n\t\"address\": "+dn.Address+"\n\t\"phone\": ");
													for(int i=0;i<10;i++){
														System.out.print(dn.phone[i]);
													}
													System.out.print("\n}\n");
												}
											}
										}
										else{
											System.out.println("Search Status for firstname: "+keyword+" - Not Found");
										}
										break;
								case "lastname":
										ret=search_lname(keyword1);
										if(ret.size()>0){
											System.out.println("Search Status for lastname: "+keyword+" -Results Found");
											for(Detail_Node dn:ret){
												if(dn==null){
													System.out.println("null");
												}
												else{
													System.out.print("{\n\t\"lastname\": "+dn.FirstName+"\n\t\"lastname\": "+dn.LastName+"\n\t\"address\": "+dn.Address+"\n\t\"phone\": ");
													for(int i=0;i<10;i++){
														System.out.print(dn.phone[i]);
													}
													System.out.print("\n}\n");
												}
											}
										}
										else{
											System.out.println("Search Status for lastname: "+keyword+" - Not Found");
										}
										break;
								case "address":
										keyword1=keyword1.replaceAll("\\s", "");
										ret=search_aname(keyword1);
										if(ret.size()>0){
											System.out.println("Search Status for address: "+keyword+" -Results Found");
											for(Detail_Node dn:ret){
												if(dn==null){
													System.out.println("null");
												}
												else{
													System.out.print("{\n\t\"address\": "+dn.FirstName+"\n\t\"lastname\": "+dn.LastName+"\n\t\"address\": "+dn.Address+"\n\t\"phone\": ");
													for(int i=0;i<10;i++){
														System.out.print(dn.phone[i]);
													}
													System.out.print("\n}\n");
												}
											}
										}
										else{
											System.out.println("Search Status for address: "+keyword+" - Not Found");
										}
										break;
								default:	break;
							}
							s1=scn.nextLine();
							s2=s1.trim();
							break;
					case "delete":	s1=scn.nextLine();
							s2=s1.trim();
							sarr=s2.split("\"");
							String num=sarr[3];
							try{
								Trie_Node dlt=delete_phone(phone_root,num,0);
								System.out.println("Delete Successful");
							}
							catch(Exception e){
								System.out.println("Unable to Delete "+e);
							}
							s1=scn.nextLine();
							s2=s1.trim();
							break;
					default: 	break;
				}
			}
		}
		catch(Exception e){
			System.out.println("Exception "+e);
		}
	}
}
