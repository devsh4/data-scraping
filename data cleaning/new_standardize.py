#author : abha

#first, standardization of project locations of a9 and bm is done using functions insert_city_id() to insert a city_id and project_id() for project_id, address_id and project_add_id.
#then, sale_rent field is standardized to contain only "Sale" and "Rent" values. 
#the area in sqft is calculated by unit conversion and then the rate in sqft is calculated.
#finally, the property_type is standardized to 5 types- apartment, plot, villa, office, shop

import re
import ConfigParser
import os
import json
import csv
import MySQLdb

globalVar = 0
globVar = 0

def insert_city_id(table_name):
    try:
        db=MySQLdb.connect(host="localhost",user="plbadm", passwd="2wsx@WSX", db="listing")
        cur = db.cursor()
        q="select _id,p_name from locations where demography='city' and p_name in (select distinct city from %s where city_id IS NULL)"
        args=(table_name)
	q1=q%args
	cur.execute(q1)
        row = cur.fetchone()
        while row is not None:
            #inserting city_id to a9 and bm from locations table when city name matches a p_name in locations
            q2="update %s set city_id=%s where city='%s' and city_id IS NULL;"
            args=(table_name,row[0],row[1])
            q= q2%args
            cur2=db.cursor()
            cur2.execute(q)
            cur2.close()
            row = cur.fetchone()		    
	    print "executing"

        cur.close()
        db.commit()
    except Exception as e:
        print(e.message,e.args)

    finally:
        db.close()


def project_id_bm(table_name):

    try:
        db=MySQLdb.connect(host="localhost",user="plbadm", passwd="2wsx@WSX", db="listing")
        cur = db.cursor()
        cur2=db.cursor()
        cur3=db.cursor()
        #q="drop view loc"
        #cur.execute(q)
        q1="select project,city_id,address from %s;"
	ar=(table_name)
	q11=q1%ar
        cur.execute(q11)
        row=cur.fetchone()
        while row is not None:
            f=0
            #print row[2]
            city_id=row[1]
            cid=None
            if city_id is not None:
                cid=city_id+".%"
                cid1='%,'+city_id+'.%'
                
            #creating a view from locations table with only those records having the same city_id as selected record from bm
            q2="create view loc as select _id,city_id,p_name,alias,demography from locations where (city_id like '%s' or city_id like '%s') and (demography is NULL or demography!='city') and _id!='246';"
            args=(cid,cid1)
            q=q2%args
            #print q
            cur2.execute(q)
            if row[0] is not None:
                #if project column in bm is not NULL, get _id from loc view and update project_id in bm corresponding to matching project name.
                project=row[0].replace("'","")
                p1='%,'+project+',%'
                p2='%,'+project
                p3=project+',%'
                q2="select _id,city_id,p_name,alias,demography from loc where p_name like '%s' or alias like '%s' or alias like '%s' or alias like '%s';"
                args=(project,p1,p2,p3)
                q=q2%args
                cur2.execute(q)
                r=cur2.fetchone()
                
                if r is not None:
                    f=1
                    _id=r[0]
                    a_id=_id+","+city_id
                    q3='update %s set project_id="%s" where address="%s" and project="%s";'
                    arg3=(table_name,r[0],row[2],project)
                    qq=q3%arg3
                    cur3.execute(qq)
                    db.commit()
                   


            
            #getting address_id and project_add_id by comparing address of projects from bm with locations table; this is useful when project column is null but project name is available in address column    
            a_id=[]
            if row[2] is not None:
                a=row[2].encode('utf-8').strip()
                b=a.replace(', ',',')
                c=b.replace(' ,',',')
                d=c.lower().replace("'","")
                alist=d.split(",")
                addr=a.replace(',','","')
                address='"'+addr+'"'
                
                #comparing separated elements of address column with loc view to get ids, putting all the ids obtained in address_id and putting the 1st id from address_id in project_add_id; max cases have project_id=project_add_id 
                for x in alist:
                    
                    x1='%,'+x+',%'
                    x2='%,'+x
                    x3=x+',%'
                    q3="select _id,p_name,alias from loc where p_name like '%s' or alias like '%s' or alias like '%s' or alias like '%s';"
                    arg3=(x,x1,x2,x3)
                    qq=q3%arg3
                    cur3.execute(qq)
                        
                    for r in cur3:
                   	if r is not None:
                            a_id.append(r[0])
                            
                            
                if city_id is not None:
                    a_id.append(city_id)
                if len(a_id)>=1:
                    _id=a_id[0]
                else:
                    _id=''
                a_ids=''
                a_ids=','.join(a_id)
                #print a_id,a_ids
                q3='update %s set project_add_id="%s",address_id="%s" where address="%s";'
                arg3=(table_name,_id,a_ids,row[2].encode('utf-8'))
                qqq=q3%arg3
                cur3.execute(qqq)
                db.commit()
	    q5="drop view loc"
            cur3.execute(q5)
            db.commit()
            row=cur.fetchone()
        
    except Exception as e:
        print(e.message,e.args)
	
    finally:
        db.commit()
        db.close()


def project_id_a9(table_name):
    try:
        db=MySQLdb.connect(host="localhost",user="plbadm", passwd="2wsx@WSX", db="listing")
        cur = db.cursor()
        cur2=db.cursor()
        cur3=db.cursor()
        #q="drop view loc"
        #cur.execute(q)
        q1="select project,city_id,address,id from %s where id>59890;"
	ar=(table_name)
	q11=q1%ar
        cur.execute(q11)
        row=cur.fetchone()
        while row is not None:
            f=0
	    #print row[0]
	    #print row[1]
	    print ""
	    print "Starting main loop"
	    print row[3]
            city_id=row[1]
            cid=None
            if city_id is not None:
                cid=city_id+".%"
                cid1='%,'+city_id+'.%'

            #creating a view from locations table with only those records having the same city_id as selected record from a9
            q2="create view loc3 as select _id,city_id,p_name,alias,demography from locations where (city_id like '%s' or city_id like '%s') and (demography is NULL or demography!='city') and _id!='246';"
            args=(cid,cid1)
            q=q2%args
            #print q
            cur2.execute(q)

            #if project column in a9 is not NULL, get _id from loc view and update project_id in a9 corresponding to matching project name.
            if row[0] is not None:
                project=row[0].replace("'","")
                p1='%,'+project+',%'
                p2='%,'+project
                p3=project+',%'

                
                q2="select _id,city_id,p_name,alias,demography from loc3 where p_name like '%s' or alias like '%s' or alias like '%s' or alias like '%s';"
                args=(project,p1,p2,p3)
                q=q2%args
                cur2.execute(q)
                print "Selecting data  from loc3"
                print q
                r=cur2.fetchone()
                
                if r is not None:
                    f=1
                    _id=r[0]
                    #a_id=_id+","+city_id
		    if row[2] is not None:
                    	q3="update %s set project_id='%s' where address='%s' and project='%s';"
                    	arg3=(table_name,r[0],row[2],project)
		    else:
			q3="update %s set project_id='%s' where address IS NULL and project='%s';"
		        arg3=(table_name,r[0],project)
                    qq=q3%arg3
		    print qq
                    cur3.execute(qq)
                    db.commit()
		    print "Commiting update project id query"
		else:
		    print "No data in loc3"
            
            #getting address_id and project_add_id by comparing address of projects from a9 with locations table; this is useful when project column is null but project name is available in address column     
            a_id=[]
	    print "Starting add_id and proj_add_id code"
	    print "addr : "+str(row[2])
	    print "listing_a9 PK id : "+ str(row[3])
            if row[2] is not None:
		a=row[2].encode('utf-8').strip()
		b=a.replace(', ',',')
                c=b.replace(' ,',',')
                d=c.lower().replace("'","")
		alist=d.split(",")
		#print alist
                addr=a.replace(',','","')
		address='"'+addr+'"'	

                #comparing separated elements of address column with loc view to get ids, putting all the ids obtained in address_id and putting the 1st id from address_id in project_add_id; max cases have project_id=project_add_id
                for x in alist:
                    x1='%,'+x+',%'
                    x2='%,'+x
                    x3=x+',%'
                    q3="select _id,p_name,alias from loc3 where p_name like '%s' or alias like '%s' or alias like '%s' or alias like '%s';"
                    arg3=(x,x1,x2,x3)
                    qq=q3%arg3
                    print qq 
                    cur3.execute(qq)
                     
		    #if cur3.rowcount > 0
		    #print r
       
		    for r in cur3:
                        print "In for r in cur 3"
			print r
			a_id.append(r[0])
			        
                            
                print "City ID : "+city_id
		if city_id is not None:   
		    a_id.append(city_id)
	            print "if city_id is not None"
                    _id=a_id[0]    
                    a_ids=','.join(a_id)
                    tempadd =  row[2].replace("'","\\'")
	            #print tempadd
		    q3="update %s set project_add_id='%s',address_id='%s' where address='%s';"
		    arg3=(table_name,_id,a_ids,tempadd)
                    qqq=q3%arg3
                    print qqq
		    cur3.execute(qqq)
                    db.commit()
		else:
		    print "city_id is null for the below address"
		    print row[2]
                print "Ending loop"
                print ""
                print ""
 
	    q5="drop view loc3"
            cur3.execute(q5)
            db.commit()
            row=cur.fetchone()
        

            
    except Exception as e:
        print(e.message,e.args)
    finally:
        db.commit()
        db.close()


def project_id_update(table_name):
    try:
        db=MySQLdb.connect(host="localhost",user="plbadm", passwd="2wsx@WSX", db="listing")
        cur = db.cursor()
        cur2=db.cursor()
        cur3=db.cursor()

      	q1="select project,city_id,address,id from %s where id >29420"
        ar=(table_name)
        q11=q1%ar
        cur.execute(q11)
        row=cur.fetchone()
        prev_city_id = -1
	while row is not None:
            f=0
            print ""
            print "Starting main loop"
            print row[3]
            city_id=row[1]
            cid=None
	    cid1=None 
            if city_id is not None:
                cid=city_id+".%"
                cid1='%,'+city_id+'.%'
	    
	    if prev_city_id != city_id:
		prev_city_id = city_id
                q5="drop view if exists loc3"
            	cur3.execute(q5)
                db.commit()	
                #creating a view from locations table with only those records having the same city_id as selected record from a9
                q2="create view loc3 as select _id,city_id,p_name,alias,demography from locations where (city_id like '%s' or city_id like '%s') and (demography is NULL or demography!='city') and _id!='246';"
                args=(cid,cid1)
                q=q2%args
                print q
                cur2.execute(q)

            #if project column in a9 is not NULL, get _id from loc view and update project_id in a9 corresponding to matching project name.
            if row[0] is not None:
                print row[0]
		project=row[0].replace("'","")
                p1='%,'+project+',%'
                p2='%,'+project
                p3=project+',%'
		
                
                q2="select _id,city_id,p_name,alias,demography from loc3 where p_name like '%s' or alias like '%s' or alias like '%s' or alias like '%s';"
                args=(project,p1,p2,p3)
                q=q2%args
                cur2.execute(q)
                print "Selecting data from loc3"
                print q
                r=cur2.fetchone()
                
                if r is not None:
                    f=1
                    _id=r[0]
                    #a_id=_id+","+city_id
                    if row[2] is not None:
		        tempadd =  row[2].replace("'","\\'")
		    else:
			print "Addr in row[2] : "+str(row[2])
                    q3="update %s set project_id='%s' where id = %s;"
                    arg3=(table_name,r[0],row[3])
                    qq=q3%arg3
                    print qq
                    cur3.execute(qq)
                    db.commit()
                    print "Commiting update project id query"
                else:
                    print "No data in loc3"
                    print "Ending loop"
                    print ""
                    print ""
	    row=cur.fetchone()
	q5="drop view loc3"
        cur3.execute(q5)
        db.commit()
            
    except Exception as e:
        print(e.message,e.args)
    finally:
        db.commit()
        db.close()

def project_id_update_sp(table_name):
    try:
        db=MySQLdb.connect(host="localhost",user="plbadm", passwd="2wsx@WSX", db="listing")
        cur = db.cursor()
        cur2=db.cursor()
        cur3=db.cursor()

        q1="select project,city_id,address,id from %s where id >59892"
        ar=(table_name)
        q11=q1%ar
        cur.execute(q11)
        row=cur.fetchone()
        prev_city_id = -1
        while row is not None:
            f=0
            print ""
            print "Starting main loop"
            print row[3]
            city_id=row[1]
            cid=None
            cid1=None
            if city_id is not None:
                cid=city_id+".%"
                cid1='%,'+city_id+'.%'

            if prev_city_id != city_id:
                prev_city_id = city_id
                q5="drop view if exists loc3"
                cur3.execute(q5)
                db.commit()
                #creating a view from locations table with only those records having the same city_id as selected record from a9
                q2="create view loc3 as select _id,city_id,p_name,alias,demography from locations where (city_id like '%s' or city_id like '%s') and (demography is NULL or demography!='city') and _id!='246';"
                args=(cid,cid1)
                q=q2%args
                print q
                cur2.execute(q)

            #if project column in a9 is not NULL, get _id from loc view and update project_id in a9 corresponding to matching project name.
            if row[0] is not None:
                print row[0]
                project=row[0].replace("'","")
                #p1='%,'+project+',%'
                #p2='%,'+project
                #p3=project+',%'
		q3="Call updateProjectId('%s','%s',%s,%s);"
		args3=(table_name,project,row[3],"@project_id")
		qq3=q3%args3
		print qq3
		cur3.execute(qq3);
		db.commit();
		q4="Select @project_id"
		cur3.execute(q4);
		rowPid = cur3.fetchone()
		print "Value of output : "+str(rowPid[0])
            row=cur.fetchone()
        q5="drop view loc3"
        cur3.execute(q5)
        db.commit()

    except Exception as e:
        print(e.message,e.args)
    finally:
        db.commit()
        db.close()


def proj_address_id_update(table_name):
    try:
        db=MySQLdb.connect(host="localhost",user="plbadm", passwd="2wsx@WSX", db="listing")
        cur = db.cursor()
        cur2=db.cursor()
        cur3=db.cursor()
        #q1="select project,city_id,address,id,project_id from %s where(project_add_id IS NULL OR address_id IS NULL) and (address IS NOT NULL OR project_id IS NOT NULL);"
        q1="select project,city_id,address,id,project_id from %s where id IN (251871,251873);"
	ar=(table_name)
        q11=q1%ar
        cur.execute(q11)
        row=cur.fetchone()
        prev_city_id = -1
        while row is not None:
            f=0
            print ""
            print "Starting main loop"
            print row[3]
            city_id=row[1]
            cid=None
            cid=None
            cid1=None 
            if city_id is not None:
                cid=city_id+".%"
                cid1='%,'+city_id+'.%'
        
            if prev_city_id != city_id:
            	prev_city_id = city_id
                q5="drop view if exists loc3"
                cur3.execute(q5)
                db.commit() 
                #creating a view from locations table with only those records having the same city_id as selected record from a9
                q2="create view loc3 as select _id,city_id,p_name,alias,demography from locations where (city_id like '%s' or city_id like '%s') and (demography is NULL or demography!='city') and _id!='246';"
                args=(cid,cid1)
                q=q2%args
                print q
                cur2.execute(q)

            a_id=[]
            print "Starting add_id and proj_add_id code"
            print "addr : "+str(row[2])
            print "listing_a9 PK id : "+ str(row[3])
            if row[2] is not None:
                a=row[2].encode('utf-8').strip()
                b=a.replace(', ',',')
                c=b.replace(' ,',',')
                d=c.lower().replace("'","")
                alist=d.split(",")
                #print alist
                addr=a.replace(',','","')
                address='"'+addr+'"'    

                #comparing separated elements of address column with loc view to get ids, putting all the ids obtained in address_id and putting the 1st id from address_id in project_add_id; max cases have project_id=project_add_id
                for x in alist:
                    x1='%,'+x+',%'
                    x2='%,'+x
                    x3=x+',%'
                    q3="select _id,p_name,alias from loc3 where p_name like '%s' or alias like '%s' or alias like '%s' or alias like '%s';"
                    arg3=(x,x1,x2,x3)
                    qq=q3%arg3
                    print qq 
                    cur3.execute(qq)
       
                    for r in cur3:
                        print "In for r in cur 3"
                        print r
                        a_id.append(r[0])
                    
                            
            print "City ID : "+str(city_id)
            a_ids=None
            if city_id is not None and len(a_id)>0:   
            	a_id.append(city_id)
            	print "city_id is not None.Appending city_id to a_id"
	    else:
                print "Either city_id is null OR a_id IS NULL for the address : "+str(row[2])
            if row[4] is not None:
                print "Project id is not null. Assigning it to project_add_id and setting address_id as NULL"
                _id=row[4]
		a_ids=None
	    elif row[2] is not None and len(a_id)>0:
		print "Project id is null & address id  is not null. Assigning 1st element to project_add_id"
		_id=a_id[0]
	    else: 
                print "Project id is null & either address id is not null or no data in a_id(loc3)"
		_id=None    
	    if row[2] is not None and len(a_id)>0:
		print "Address is not null. Assigning respective address ID"
		a_ids=','.join(a_id)
            #tempadd =  row[2].replace("'","\\'")
            #print tempadd
	    print "Will update now"
	    if a_ids is not None and _id is not None:
                q3="update %s set project_add_id='%s',address_id='%s' where id = %s;"
		arg3=(table_name,_id,a_ids,row[3])
	    elif _id is not None:
		q3="update %s set project_add_id='%s',address_id=NULL where id = %s;"
                arg3=(table_name,_id,row[3])
	    else: 
		print "a_ids and _id both are none. Setting q3 as none"
		q3=None 
            if q3 is not None:
	    	qqq=q3%arg3
            	print qqq
            	cur3.execute(qqq)
            	db.commit()
            else:
		print "q3 is None. Not executing update query."
	    print "Ending loop"
            print ""
            print ""
            row=cur.fetchone()

        q5="drop view loc3"
        cur3.execute(q5)
        db.commit()
            
    except Exception as e:
        print(e.message,e.args)
    finally:
        db.commit()
        db.close()

def sale_rent(table_name):
    try:
        db=MySQLdb.connect(host="localhost",user="plbadm", passwd="2wsx@WSX", db="listing")
        cur = db.cursor()
        q1="update %s set sale_rent='Sale' where sale_rent='Buy' or sale_rent='sale'"
        arg=(table_name)
        q=q1%arg
        cur.execute(q)
        q2="update %s set sale_rent='Rent' where sale_rent='Lease' or sale_rent='rent'"
        qq=q2%arg
        cur.execute(qq)
        

        cur.close()
        db.commit()
    except Exception as e:
        print(e.message,e.args)

    finally:
        db.close()

def area_sqft():
    try:
        db=MySQLdb.connect(host="localhost",user="plbadm", passwd="2wsx@WSX", db="listing")
        cur = db.cursor()

	#for a9
        cur.execute(q2)
        q3="UPDATE listing_a9 SET superbuilt_area_sqft= 10.7639*super_built_area WHERE area_unit LIKE 'sq%m%';"
        cur.execute(q3)
        q4="UPDATE listing_a9 SET superbuilt_area_sqft= 43560*super_built_area WHERE area_unit LIKE 'acre%';"
        cur.execute(q4)
        q5="UPDATE listing_a9 SET superbuilt_area_sqft= 272.251*super_built_area WHERE area_unit LIKE 'marla';"
        cur.execute(q5)
        q6="update listing_a9 set rate_sqft=price/superbuilt_area_sqft;"
	cur.execute(q6)


        #for bm
	q1="UPDATE listing_bm SET superbuilt_area_sqft= super_built_area WHERE area_unit LIKE 'sq%ft%';"
        cur.execute(q1)
        q2="UPDATE listing_bm SET superbuilt_area_sqft= 9*super_built_area WHERE area_unit LIKE 'sq%yrd%' or area_unit LIKE 'sq%yard%';"
        cur.execute(q2)
        q3="UPDATE listing_bm SET superbuilt_area_sqft= 10.7639*super_built_area WHERE area_unit LIKE 'sq%m%';"
        cur.execute(q3)
        q4="UPDATE listing_bm SET superbuilt_area_sqft= 43560*super_built_area WHERE area_unit LIKE 'acre%';"
        cur.execute(q4)
        q5="UPDATE listing_bm SET superbuilt_area_sqft= 272.251*super_built_area WHERE area_unit LIKE 'marla';"
        cur.execute(q5)
        q6="update listing_bm set rate_sqft=price/superbuilt_area_sqft;"
	cur.execute(q6)

	q6="UPDATE listing_bm SET superbuilt_area_sqft=435.61545*super_built_area WHERE area_unit LIKE 'cent%';"
        cur.execute(q6)
        q7="UPDATE listing_bm SET superbuilt_area_sqft=1089.08734*super_built_area WHERE area_unit LIKE 'guntha%';"
        cur.execute(q7)
        q8="UPDATE listing_bm SET superbuilt_area_sqft=17452.00698*super_built_area WHERE area_unit LIKE 'bigha%';"
        cur.execute(q8)
        q10="UPDATE listing_bm SET superbuilt_area_sqft=357142.8571*super_built_area WHERE area_unit LIKE 'biswa%';"
        cur.execute(q10)
        q11="UPDATE listing_bm SET superbuilt_area_sqft=2400*super_built_area WHERE area_unit LIKE 'ground%';"
        cur.execute(q11)
        q12="UPDATE listing_bm SET superbuilt_area_sqft=5445*super_built_area WHERE area_unit LIKE 'kanal%';"
        cur.execute(q12)

        q6="update listing_bm set rate_sqft=price/superbuilt_area_sqft where price!='/';"
	cur.execute(q6)



        cur.close()
        db.commit()
    except Exception as e:
        print(e.message,e.args)

    finally:
        db.close()


def property_type_a9():
    try:
        db=MySQLdb.connect(host="localhost",user="plbadm", passwd="2wsx@WSX", db="listing")
	q1="update listing_a9 set property_type_norm='Office' where property_type like '%office%';"
        cur.execute(q1)
        q2="update listing_a9 set property_type_norm='Villa' where property_type like '%villa%';"
        cur.execute(q2)
        q3="update listing_a9 set property_type_norm='Shop' where property_type like '%shop%' or property_type like '%commercial%showroom%';"
        cur.execute(q3)
        q4="update listing_a9 set property_type_norm='Plot' where property_type like '%plot%' or property_type like '%land%' or property_type like 'farm house';"
        cur.execute(q4)
        q5="update listing_a9 set property_type_norm='Apartment' where property_type like '%apartment%' or property_type like 'Independent/Builder Floor' or property_type like 'penthouse';"
        cur.execute(q5)
        
        q5="update listing_a9 set property_type_norm='Other' where property_type='Other';"
        cur.execute(q5)
        
        cur.execute(q5)
        
        cur.close()
        db.commit()
    except Exception as e:
        print(e.message,e.args)
        db.commit()
    except Exception as e:
        print(e.message,e.args)

    finally:
        db.close()


def property_type_bm():
    try:
        db=MySQLdb.connect(host="localhost",user="plbadm", passwd="2wsx@WSX", db="listing")
        cur = db.cursor()
        q1="update listing_bm set property_type_norm='Office' where property_type like '%office%';"
        q2="update listing_bm set property_type_norm='Villa' where property_type like '%villa%';"
        cur.execute(q2)
        q3="update listing_bm set property_type_norm='Shop' where property_type like '%shop%' or property_type like '%commercial%showroom%';"
        cur.execute(q3)
        q4="update listing_bm set property_type_norm='Plot' where property_type like '%plot%' or property_type like '%land%' or property_type like 'farm house';"
        cur.execute(q4)
        q5="update listing_bm set property_type_norm='Apartment' where property_type like '%apartment%' or property_type like 'residential house' or property_type like 'penthouse';"
        cur.execute(q5)
        
        q5="update listing_bm set property_type_norm='Industrial Building' where property_type='Industrial Building';" 
        cur.execute(q5)

        q6="update listing_bm set property_type_norm='Industrial Shed' where property_type='Industrial Shed';"
        cur.execute(q6)
	q7="update listing_bm set property_type_norm='Warehouse/ Godown' where property_type='Warehouse/ Godown';"
        cur.execute(q7)
	
        cur.close()
        db.commit()
    except Exception as e:
        print(e.message,e.args)

    finally:
        db.close()

        
if __name__ == "__main__":
	
    project_id_update_sp("listing_a9")
    #proj_address_id_update("test_listing_a9")

   #  insert_city_id("listing_a9")
   #  insert_city_id("listing_bm")
   # project_id_bm("listing_bm")
   # project_id_a9("listing_a9")
   # sale_rent("listing_a9")
   # sale_rent("listing_bm")
   # area_sqft()
   # property_type_a9()
   # p
