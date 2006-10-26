//------------------------------------------------------------------------------
// NWSRFS_PDBINDEX - class to contain the preprocessor 
// index/record pointers
//------------------------------------------------------------------------------
// History:
//
// 2004-10-14	Scott Townsend, RTi	Initial version.
//------------------------------------------------------------------------------
// Endheader

package RTi.DMI.NWSRFS_DMI;

import java.util.Vector;

/**
The NWSRFS_PDBINDEX - class to contain the preprocessor record 
pointers and is used to increase performance in reading the files PDBLYn and 
PDBRRS by retrieving the record number for a specific time series used in those 
files. This class reads and stores data from the PDBINDEX preprocessor 
database file; it stores the entire contents of PDBINDEX in this object. The 
PDBINDEX database file has the following definition:
<pre>
IX.4.2B-PDBINDEX    PREPROCESSOR DATA BASE FILE PDBINDEX

Purpose
File PDBINDEX contains information about stations defined in the
Preprocessor Data Base (PPDB).

The information is used to access the station data and includes
control information for dates and record pointers for the daily data
types.

Description

ATTRIBUTES: fixed length 64 byte binary records

RECORD STRUCTURE:
                                  Word
  Variable Type     Dimension   Position   Description

  The first record is the File Control Record.

  NWRDS       I*4       1          1       Number of words in record

  LRECL1      I*4       1          2       Logical record length of daily
                                           data files

  LRECL2      I*4       1          3       Logical record length of RRS
                                           file

  LRECL3      I*4       1          4       Logical record length of Index
                                           file

  MAXTYP      I*4       1          5       Maximum number of daily data
                                           types

  NUMTYP      I*4       1          6       Actual number of daily data
                                           types

  TYPREC      I*4       1          7       Record of the first Data Type
                                           Directory record

  NHASHR      I*4       1          8       Number of I*2 words in hash
                                           records

  H8CREC      I*4       1          9       Record number of the first
                                           character hash record

  HINTRC      I*4       1          10      Record number of the first
                                           integer hash record

  INFREC      I*4       1          11      Record number of the first
                                           Station Information record
                                          (first record used is
                                          INFREC+1)

  MFILE      I*4       1          12      Maximum records in file

  LFILE      I*4       1          13      Last used record in file

  LURRS      I*4       1          14      Maximum days between last day
                                          of observed data and first day
                                          of future data

  MAXDDF     I*4       1          15      Maximum number of daily data
                                          files

  NUMDDF     I*4       1          16      Number of daily data files
                                          used

  The next group of records are the data file information records.

  Words 1 thru 4 are repeated NUMDDF times:

  MDDFRC     I*4       1          1       Maximum records in daily data
                                          file

  LDDFRC     I*4       1          2       Last record used in daily data
                                          file

             I*4       2         3-4      Unused

  The next group of records are the Daily Data Type Directory records.

  NWRDS      I*2       1          1       Number of words in record

  DTYPE      A4        1         2-3      Data type

  LUFILE     I*2       1          4       Logical unit of file in which
                                          data is stored

  NPNTRS     I*2       1          5       See note 1/

  NDATA      I*2       1          6       Number of data values for each
                                          station (can vary for data
                                          types PPVR and TAVR)

  MAXDAY     I*2       1          7       Maximum days of data

  EDATE      I*4       1         8-9      Julian day of earliest data

  ECRECN     I*2       1          10      Record number of earliest data
                                          (not used for TF24)

  LDATE      I*4       1        11-12     Julian day of latest data (not
                                          used for TF24)

  LDRECN     I*2       1          13      Record number of latest data

  PNTR       I*2       1          14      Record number of pointer
                                          record

  DATAR1     I*2       1          15      Record number of first data
                                          record

  MAXSTA     I*2       1          16      Maximum number of stations

  NUMSTA     I*2       1          17      Number of stations defined

  LSTPTR     I*2       1          18      Last used word for pointer
                                          record

  LSTDTA     I*2       1          19      Last used word in data record

  NSTATS     I*2       1          20      Number of words of statistics
                                          per station for data type

  NREC1D     I*2       1          21      Number of data records for one
                                          day of data

             I*2       2         22-24    Unused

  The next group of records contain the Station Hash Indexes. 2/

  IPDHSC     I*2    NHASHR         1      Record numbers of Station
                                          Information records for
                                          station character identifiers

  IPDHSI     I*2    NHASHR         1      Record numbers of Station
                                          Information records for
                                          station integer identifiers

  The next group of records are the Station Information records. 3/

  NWRDS      I*2       1           1      Number of words in record

  STAID      A8       1           2-5     Station character identifier

  NUMID      I*2       1           6      Station integer identifier

  PRMPTR     I*2       1           7      Record number of GENL
                                          parameter record in
                                          Preprocessor Parametric Data
                                          Base

  PCPPTR     I*2       1           8      Array location of 24 hour
                                          precipitation data 4/


  TMPPTR     I*2       1           9       Array location of 24 hour
                                           temperature max/min data 4/

  NADDTP     I*2       1           10      Number of additional data
                                           types

  Words 11 thru 13 are repeated NADDTP times.

  ADDDTP     A4        1         11-12     Data type 4/

  ADTPTR     I*2       1           13      Array location if daily data
                                           type or record number of RRS
                                           primary data 4/ 5/

  The following statistic are stored for stations with 24 hour PCPN
  data:

  BDATE      I*2       2       11+NADDTP*3 Julian day statistics begin

  RDATE      I*2       2       13+NADDTP*3 Julian day of most recent
                                           report

  NDAYS      I*2       1       15+NADDTP*3 Number of days with reports

  NTOTAL     I*2       1       16+NADDTP*3 Total number of reports

  NZERO      I*2       1       17+NADDTP*3 Number of days that zero
                                           precipitation is reported

  ACDCP      R*4       1       18+NADDTP*3 Accumulated precipitation

  RPTLG      I*2       1       20+NADDTP*3 Largest reported value (stored
                                           as value*100)

  LDATE      I*2       2       21+NADDTP*3 Julian day of largest reported
                                           value

  RPT2LG     I*2       1       23+NADDTP*3 Second largest value reported
                                           (stored as value*100)

  L2DATE     I*2       2       24+NADDTP*3 Julian day of second largest
                                           reported value

  SMNOZO     I*2       1       26+NADDTP*3 Smallest non-zero report

  ACPSQ      R*4       1       27+NADDTP*3 Accumulated precipitation
                                           squared

  The following word is used for stations that have been redefined:

  NWRDSO     I*2       1        NWRDS+1    Number of words in old SIF
                                           entry

Notes:

1/ For data types PP24, PPVR, TAVR, TM24, MDR6, TF24, EA24, PPST, APIG
   and PG24 the value is the number of pointers.

   For data types TX24 and TN24 the value is the indicator for the
   write data types in read data type TM24.

   For data types TFMX and TFMN the value is the indicator for the
   write data types in read data type TF24.

   For data types PP01, PP03, PP06, TA01, TA03 and TA06 the value is
   the data time interval for the write only types.

   For data types TA24, TD24, US24, RC24, RP24 and RI24 the value is
   the indicator for the write only types in read data type EA24.

   For data type PPSR the value is the number of words in the one
   pointer record needed for this type.

2/ Access to the Station Information records is through a hashing
   algorithm. The hash can be done using the 8-character station
   identifier or the user-assigned integer station number. The first
   set of records stores the hashed indices for the 8-character
   station identifier. The second set of records stores the hashed
   indices for the user-assigned integer station number. These hashed
   indices point to the Station Information record in another part of
   the file.

3/ The Station Information records contain pointers to the data for
   each data type reported by a station. For stations with PCPN data
   they also have room for statistical information.

4/ For Daily data types the value stored is the starting location of
   the data in the data array returned from the PPDB read daily data
   routine (RPDDLY) for the specified data type.

5/ For RRS data types the value stored is the record number of the
   data in the RRS primary data file.


                IX.4.2B-PDBINDEX-5
</pre>
*/

public class NWSRFS_PDBINDEX {

protected int _H8CREC;
protected int _HINTRC;
protected int _INFREC;
protected int _LFILE;
protected int _LRECL1;
protected int _LRECL2;
protected int _LRECL3;
protected int _LURRS;
protected int _MAXDDF;
protected int _MAXTYP;
protected int _MFILE;
protected int _NHASHR;
protected int _NUMDDF;
protected int _NUMTYP;
protected int _NWRDS;
protected int _TYPREC;
protected Vector _ACDCP;
protected Vector _ACPSQ;
protected Vector _ADDDTP;
protected Vector _ADTPTR;
protected Vector _BDATE;
protected Vector _DATAR1;
protected Vector _DTYPE;
protected Vector _ECRECN;
protected Vector _EDATE;
protected Vector _L2DATE;
protected Vector _LDATE;
protected Vector _LDATEDDT;
protected Vector _LDDFRC;
protected Vector _LDRECN;
protected Vector _LSTDTA;
protected Vector _LSTPTR;
protected Vector _LUFILE;
protected Vector _MAXDAY;
protected Vector _MAXSTA;
protected Vector _MDDFRC;
protected Vector _NADDTP;
protected Vector _NDATA;
protected Vector _NDAYS;
protected Vector _NPNTRS;
protected Vector _NREC1D;
protected Vector _NSTATS;
protected Vector _NTOTAL;
protected Vector _NUMID;
protected Vector _NUMSTA;
protected Vector _NWRDSDDT;
protected Vector _NWRDSSTI;
protected Vector _NWRDSO;
protected Vector _NZERO;
protected Vector _PCPPTR;
protected Vector _PNTR;
protected Vector _PRMPTR;
protected Vector _RDATE;
protected Vector _RPT2LG;
protected Vector _RPTLG;
protected Vector _SMNOZO;
protected Vector _STAID;
protected Vector _TMPPTR;

/**
Constructor.
If the calling class uses this constructor then it will need to call the 
readFile method manually.  This constructor is needed to allow multiple calls 
through the same DMI object.
*/
public NWSRFS_PDBINDEX() {
	initialize();
}

public void addACDCP(float ACDCP) {
	_ACDCP.addElement(new Float(ACDCP)); 
}

public void addACPSQ(float ACPSQ) {
	_ACPSQ.addElement(new Float(ACPSQ)); 
}

public void addADDDTP(Vector ADDDTP) {
	_ADDDTP.addElement(ADDDTP); 
}

public void addADTPTR(Vector ADTPTR) {
	_ADTPTR.addElement(ADTPTR); 
}

public void addBDATE(int BDATE) {
	_BDATE.addElement(new Integer(BDATE)); 
}

public void addDATAR1(int DATAR1) {
	_DATAR1.addElement(new Integer(DATAR1)); 
}

public void addDTYPE(String DTYPE) {
	_DTYPE.addElement(DTYPE); 
}

public void addECRECN(int ECRECN) {
	_ECRECN.addElement(new Integer(ECRECN)); 
}

public void addEDATE(int EDATE) {
	_EDATE.addElement(new Integer(EDATE)); 
}

public void addL2DATE(int L2DATE) {
	_L2DATE.addElement(new Integer(L2DATE)); 
}

public void addLDATE(int LDATE) {
	_LDATE.addElement(new Integer(LDATE)); 
}

public void addLDATEDDT(int LDATEDDT) {
	_LDATEDDT.addElement(new Integer(LDATEDDT)); 
}

public void addLDDFRC(int LDDFRC) {
	_LDDFRC.addElement(new Integer(LDDFRC)); 
}

public void addLDRECN(int LDRECN) {
	_LDRECN.addElement(new Integer(LDRECN)); 
}

public void addLSTDTA(int LSTDTA) {
	_LSTDTA.addElement(new Integer(LSTDTA)); 
}

public void addLSTPTR(int LSTPTR) {
	_LSTPTR.addElement(new Integer(LSTPTR)); 
}

public void addLUFILE(int LUFILE) {
	_LUFILE.addElement(new Integer(LUFILE)); 
}

public void addMAXDAY(int MAXDAY) {
	_MAXDAY.addElement(new Integer(MAXDAY)); 
}

public void addMAXSTA(int MAXSTA) {
	_MAXSTA.addElement(new Integer(MAXSTA)); 
}

public void addMDDFRC(int MDDFRC) {
	_MDDFRC.addElement(new Integer(MDDFRC)); 
}

public void addNADDTP(int NADDTP) {
	_NADDTP.addElement(new Integer(NADDTP)); 
}

public void addNDATA(int NDATA) {
	_NDATA.addElement(new Integer(NDATA)); 
}

public void addNDAYS(int NDAYS) {
	_NDAYS.addElement(new Integer(NDAYS)); 
}

public void addNPNTRS(int NPNTRS) {
	_NPNTRS.addElement(new Integer(NPNTRS)); 
}

public void addNREC1D(int NREC1D) {
	_NREC1D.addElement(new Integer(NREC1D)); 
}

public void addNSTATS(int NSTATS) {
	_NSTATS.addElement(new Integer(NSTATS)); 
}

public void addNTOTAL(int NTOTAL) {
	_NTOTAL.addElement(new Integer(NTOTAL)); 
}

public void addNUMID(int NUMID) {
	_NUMID.addElement(new Integer(NUMID)); 
}

public void addNUMSTA(int NUMSTA) {
	_NUMSTA.addElement(new Integer(NUMSTA)); 
}

public void addNWRDSDDT(int NWRDSDDT) {
	_NWRDSDDT.addElement(new Integer(NWRDSDDT)); 
}

public void addNWRDSSTI(int NWRDSSTI) {
	_NWRDSSTI.addElement(new Integer(NWRDSSTI)); 
}

public void addNWRDSO(int NWRDSO) {
	_NWRDSO.addElement(new Integer(NWRDSO)); 
}

public void addNZERO(int NZERO) {
	_NZERO.addElement(new Integer(NZERO)); 
}

public void addPCPPTR(int PCPPTR) {
	_PCPPTR.addElement(new Integer(PCPPTR)); 
}

public void addPNTR(int PNTR) {
	_PNTR.addElement(new Integer(PNTR)); 
}

public void addPRMPTR(int PRMPTR) {
	_PRMPTR.addElement(new Integer(PRMPTR)); 
}

public void addRDATE(int RDATE) {
	_RDATE.addElement(new Integer(RDATE)); 
}

public void addRPT2LG(int RPT2LG) {
	_RPT2LG.addElement(new Integer(RPT2LG)); 
}

public void addRPTLG(int RPTLG) {
	_RPTLG.addElement(new Integer(RPTLG)); 
}

public void addSMNOZO(int SMNOZO) {
	_SMNOZO.addElement(new Integer(SMNOZO)); 
}

public void addSTAID(String STAID) {
	_STAID.addElement(STAID); 
}

public void addTMPPTR(int TMPPTR) {
	_TMPPTR.addElement(new Integer(TMPPTR)); 
}

/**
Cleans up member variables.
@throws Throwable if an error occurs.
*/
public void finalize() {
	_ACDCP 		= null;
	_ACPSQ 		= null;
	_ADDDTP 	= null;
	_ADTPTR 	= null;
	_BDATE 		= null;
	_DATAR1 	= null;
	_DTYPE 		= null;
	_ECRECN 	= null;
	_EDATE 		= null;
	_H8CREC 	= -1;
	_HINTRC 	= -1;
	_INFREC 	= -1;
	_L2DATE 	= null;
	_LDATE 		= null;
	_LDATEDDT 	= null;
	_LDDFRC 	= null;
	_LDRECN 	= null;
	_LFILE 		= -1;
	_LRECL1 	= -1;
	_LRECL2 	= -1;
	_LRECL3 	= -1;
	_LSTDTA 	= null;
	_LSTPTR 	= null;
	_LUFILE 	= null;
	_LURRS 		= -1;
	_MAXDAY 	= null;
	_MAXDDF 	= -1;
	_MAXSTA 	= null;
	_MAXTYP 	= -1;
	_MDDFRC 	= null;
	_MFILE 		= -1;
	_NADDTP 	= null;
	_NDATA 		= null;
	_NDAYS 		= null;
	_NHASHR 	= -1;
	_NPNTRS 	= null;
	_NREC1D 	= null;
	_NSTATS 	= null;
	_NTOTAL 	= null;
	_NUMDDF 	= -1;
	_NUMID 		= null;
	_NUMSTA 	= null;
	_NUMTYP 	= -1;
	_NWRDS 		= -1;
	_NWRDSDDT 	= null;
	_NWRDSSTI 	= null;
	_NWRDSO 	= null;
	_NZERO 		= null;
	_PCPPTR 	= null;
	_PNTR 		= null;
	_PRMPTR 	= null;
	_RDATE 		= null;
	_RPT2LG 	= null;
	_RPTLG 		= null;
	_SMNOZO 	= null;
	_STAID 		= null;
	_TMPPTR 	= null;
	_TYPREC 	= -1;
}

public int getH8CREC() {
	return _H8CREC; 
}

public int getHINTRC() {
	return _HINTRC; 
}

public int getINFREC() {
	return _INFREC; 
}

/**
This method determine whether or not a PDB data type is an RRS data type or
not. If it is not then it is a Daily Data type.
@param dataType the data type to check.
@return a boolean - true if it is a RRS data type -- false otherwise.
*/
public static boolean getIsRRSType(String dataType) {

	if(dataType.equalsIgnoreCase("AESC")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("DQIN")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("DQME")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("FBEL")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("FGDP")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("GATE")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("GTCS")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("ICET")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("LAKH")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("LELV")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("NFBD")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("PCFD")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("PELV")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("QIN")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("QME")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("RQGM")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("RQIM")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("RQIN")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("RQME")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("RQOT")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("RQSW")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("RSTO")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("SNOG")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("SNWE")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("STG")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("TID")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("TWEL")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("TWSW")) {
		return true;
	}
	else if(dataType.equalsIgnoreCase("ZELV")) {
		return true;
	}

	return false;
}

public int getLFILE() {
	return _LFILE; 
}

public int getLRECL1() {
	return _LRECL1; 
}

public int getLRECL2() {
	return _LRECL2; 
}

public int getLRECL3() {
	return _LRECL3; 
}

public int getLURRS() {
	return _LURRS; 
}

public int getMAXDDF() {
	return _MAXDDF; 
}

public int getMAXTYP() {
	return _MAXTYP; 
}

public int getMFILE() {
	return _MFILE; 
}

public int getNHASHR() {
	return _NHASHR; 
}

public int getNUMDDF() {
	return _NUMDDF; 
}

public int getNUMTYP() {
	return _NUMTYP; 
}

public int getNWRDS() {
	return _NWRDS; 
}

public int getTYPREC() {
	return _TYPREC; 
}

public Vector getACDCP() {
	return _ACDCP; 
}  
public float getACDCP(int ACDCPindex) { 
	return (float)((Float)_ACDCP.elementAt(ACDCPindex)).floatValue();
}

public Vector getACPSQ() {
	return _ACPSQ; 
}  
public float getACPSQ(int ACPSQindex) { 
	return (float)((Float)_ACPSQ.elementAt(ACPSQindex)).floatValue();
}

public Vector getADDDTP() {
	return _ADDDTP; 
}  
public Vector getADDDTP(int ADDDTPVindex) { 
	return (Vector)_ADDDTP.elementAt(ADDDTPVindex);
}
public String getADDDTP(int ADDDTPVindex,int ADDDTPindex) { 
	Vector adddtpVect = (Vector)_ADDDTP.elementAt(ADDDTPVindex);
	return (String)adddtpVect.elementAt(ADDDTPindex);
}

public Vector getADTPTR() {
	return _ADTPTR; 
}  
public Vector getADTPTR(int ADTPTRVindex) { 
	return (Vector)_ADTPTR.elementAt(ADTPTRVindex);
}
public int getADTPTR(int ADTPTRVindex, int ADTPTRindex) { 
	Vector adtptrVect = (Vector)_ADTPTR.elementAt(ADTPTRVindex);
	return (int)((Integer)adtptrVect.elementAt(ADTPTRindex)).intValue();
}

public Vector getBDATE() {
	return _BDATE; 
}  
public int getBDATE(int BDATEindex) { 
	return (int)((Integer)_BDATE.elementAt(BDATEindex)).intValue();
}

public Vector getDATAR1() {
	return _DATAR1; 
}  
public int getDATAR1(int DATAR1index) { 
	return (int)((Integer)_DATAR1.elementAt(DATAR1index)).intValue();
}

public Vector getDTYPE() {
	return _DTYPE; 
}  
public String getDTYPE(int DTYPEindex) { 
	return (String)_DTYPE.elementAt(DTYPEindex);
}

public Vector getECRECN() {
	return _ECRECN; 
}  
public int getECRECN(int ECRECNindex) { 
	return (int)((Integer)_ECRECN.elementAt(ECRECNindex)).intValue();
}

public Vector getEDATE() {
	return _EDATE; 
}  
public int getEDATE(int EDATEindex) { 
	return (int)((Integer)_EDATE.elementAt(EDATEindex)).intValue();
}

public Vector getL2DATE() {
	return _L2DATE; 
}  
public int getL2DATE(int L2DATEindex) { 
	return (int)((Integer)_L2DATE.elementAt(L2DATEindex)).intValue();
}

public Vector getLDATE() {
	return _LDATE; 
}  
public int getLDATE(int LDATEindex) { 
	return (int)((Integer)_LDATE.elementAt(LDATEindex)).intValue();
}

public Vector getLDATEDDT() {
	return _LDATEDDT; 
}  
public int getLDATEDDT(int LDATEDDTindex) { 
	return (int)((Integer)_LDATEDDT.elementAt(LDATEDDTindex)).intValue();
}

public Vector getLDDFRC() {
	return _LDDFRC; 
}  
public int getLDDFRC(int LDDFRCindex) { 
	return (int)((Integer)_LDDFRC.elementAt(LDDFRCindex)).intValue();
}

public Vector getLDRECN() {
	return _LDRECN; 
}  
public int getLDRECN(int LDRECNindex) { 
	return (int)((Integer)_LDRECN.elementAt(LDRECNindex)).intValue();
}

public Vector getLSTDTA() {
	return _LSTDTA; 
}  
public int getLSTDTA(int LSTDTAindex) { 
	return (int)((Integer)_LSTDTA.elementAt(LSTDTAindex)).intValue();
}

public Vector getLSTPTR() {
	return _LSTPTR; 
}  
public int getLSTPTR(int LSTPTRindex) { 
	return (int)((Integer)_LSTPTR.elementAt(LSTPTRindex)).intValue();
}

public Vector getLUFILE() {
	return _LUFILE; 
}  
public int getLUFILE(int LUFILEindex) { 
	return (int)((Integer)_LUFILE.elementAt(LUFILEindex)).intValue();
}

public Vector getMAXDAY() {
	return _MAXDAY; 
}  
public int getMAXDAY(int MAXDAYindex) { 
	return (int)((Integer)_MAXDAY.elementAt(MAXDAYindex)).intValue();
}

public Vector getMAXSTA() {
	return _MAXSTA; 
}  
public int getMAXSTA(int MAXSTAindex) { 
	return (int)((Integer)_MAXSTA.elementAt(MAXSTAindex)).intValue();
}

public Vector getMDDFRC() {
	return _MDDFRC; 
}  
public int getMDDFRC(int MDDFRCindex) { 
	return (int)((Integer)_MDDFRC.elementAt(MDDFRCindex)).intValue();
}

public Vector getNADDTP() {
	return _NADDTP; 
}  
public int getNADDTP(int NADDTPindex) { 
	return (int)((Integer)_NADDTP.elementAt(NADDTPindex)).intValue();
}

public Vector getNDATA() {
	return _NDATA; 
}  
public int getNDATA(int NDATAindex) { 
	return (int)((Integer)_NDATA.elementAt(NDATAindex)).intValue();
}

public Vector getNDAYS() {
	return _NDAYS; 
}  
public int getNDAYS(int NDAYSindex) { 
	return (int)((Integer)_NDAYS.elementAt(NDAYSindex)).intValue();
}

public Vector getNPNTRS() {
	return _NPNTRS; 
}  
public int getNPNTRS(int NPNTRSindex) { 
	return (int)((Integer)_NPNTRS.elementAt(NPNTRSindex)).intValue();
}

public Vector getNREC1D() {
	return _NREC1D; 
}  
public int getNREC1D(int NREC1Dindex) { 
	return (int)((Integer)_NREC1D.elementAt(NREC1Dindex)).intValue();
}

public Vector getNSTATS() {
	return _NSTATS; 
}  
public int getNSTATS(int NSTATSindex) { 
	return (int)((Integer)_NSTATS.elementAt(NSTATSindex)).intValue();
}

public Vector getNTOTAL() {
	return _NTOTAL; 
}  
public int getNTOTAL(int NTOTALindex) { 
	return (int)((Integer)_NTOTAL.elementAt(NTOTALindex)).intValue();
}

public Vector getNUMID() {
	return _NUMID; 
}  
public int getNUMID(int NUMIDindex) { 
	return (int)((Integer)_NUMID.elementAt(NUMIDindex)).intValue();
}

public Vector getNUMSTA() {
	return _NUMSTA; 
}  
public int getNUMSTA(int NUMSTAindex) { 
	return (int)((Integer)_NUMSTA.elementAt(NUMSTAindex)).intValue();
}

public Vector getNWRDSDDT() {
	return _NWRDSDDT; 
}  
public int getNWRDSDDT(int NWRDSDDTindex) { 
	return (int)((Integer)_NWRDSDDT.elementAt(NWRDSDDTindex)).intValue();
}

public Vector getNWRDSSTI() {
	return _NWRDSSTI; 
}  
public int getNWRDSSTI(int NWRDSSTIindex) { 
	return (int)((Integer)_NWRDSSTI.elementAt(NWRDSSTIindex)).intValue();
}

public Vector getNWRDSO() {
	return _NWRDSO; 
}  
public int getNWRDSO(int NWRDSOindex) { 
	return (int)((Integer)_NWRDSO.elementAt(NWRDSOindex)).intValue();
}

public Vector getNZERO() {
	return _NZERO; 
}  
public int getNZERO(int NZEROindex) { 
	return (int)((Integer)_NZERO.elementAt(NZEROindex)).intValue();
}

public Vector getPCPPTR() {
	return _PCPPTR; 
}  
public int getPCPPTR(int PCPPTRindex) { 
	return (int)((Integer)_PCPPTR.elementAt(PCPPTRindex)).intValue();
}

public Vector getPNTR() {
	return _PNTR; 
}  
public int getPNTR(int PNTRindex) { 
	return (int)((Integer)_PNTR.elementAt(PNTRindex)).intValue();
}

public Vector getPRMPTR() {
	return _PRMPTR; 
}  
public int getPRMPTR(int PRMPTRindex) { 
	return (int)((Integer)_PRMPTR.elementAt(PRMPTRindex)).intValue();
}

public Vector getRDATE() {
	return _RDATE; 
}  
public int getRDATE(int RDATEindex) { 
	return (int)((Integer)_RDATE.elementAt(RDATEindex)).intValue();
}

public Vector getRPT2LG() {
	return _RPT2LG; 
}  
public int getRPT2LG(int RPT2LGindex) { 
	return (int)((Integer)_RPT2LG.elementAt(RPT2LGindex)).intValue();
}

public Vector getRPTLG() {
	return _RPTLG; 
}  
public int getRPTLG(int RPTLGindex) { 
	return (int)((Integer)_RPTLG.elementAt(RPTLGindex)).intValue();
}

public Vector getSMNOZO() {
	return _SMNOZO; 
}  
public int getSMNOZO(int SMNOZOindex) { 
	return (int)((Integer)_SMNOZO.elementAt(SMNOZOindex)).intValue();
}

public Vector getSTAID() {
	return _STAID; 
}  
public String getSTAID(int STAIDindex) { 
	return (String)_STAID.elementAt(STAIDindex);
}

public Vector getTMPPTR() {
	return _TMPPTR; 
}  
public int getTMPPTR(int TMPPTRindex) { 
	return (int)((Integer)_TMPPTR.elementAt(TMPPTRindex)).intValue();
}

/**
Initialize global objects.
*/
private void initialize() {
	_ACDCP 		= new Vector();
	_ACPSQ 		= new Vector();
	_ADDDTP 	= new Vector();
	_ADTPTR 	= new Vector();
	_BDATE 		= new Vector();
	_DATAR1 	= new Vector();
	_DTYPE 		= new Vector();
	_ECRECN 	= new Vector();
	_EDATE 		= new Vector();
	_H8CREC 	= -1;
	_HINTRC 	= -1;
	_INFREC 	= -1;
	_L2DATE 	= new Vector();
	_LDATE 		= new Vector();
	_LDATEDDT 	= new Vector();
	_LDDFRC 	= new Vector();
	_LDRECN 	= new Vector();
	_LFILE 		= -1;
	_LRECL1 	= -1;
	_LRECL2 	= -1;
	_LRECL3 	= -1;
	_LSTDTA 	= new Vector();
	_LSTPTR 	= new Vector();
	_LUFILE 	= new Vector();
	_LURRS 		= -1;
	_MAXDAY 	= new Vector();
	_MAXDDF 	= -1;
	_MAXSTA 	= new Vector();
	_MAXTYP 	= -1;
	_MDDFRC 	= new Vector();
	_MFILE 		= -1;
	_NADDTP 	= new Vector();
	_NDATA 		= new Vector();
	_NDAYS 		= new Vector();
	_NHASHR 	= -1;
	_NPNTRS 	= new Vector();
	_NREC1D 	= new Vector();
	_NSTATS 	= new Vector();
	_NTOTAL 	= new Vector();
	_NUMDDF 	= -1;
	_NUMID 		= new Vector();
	_NUMSTA 	= new Vector();
	_NUMTYP 	= -1;
	_NWRDS 		= -1;
	_NWRDSDDT 	= new Vector();
	_NWRDSSTI 	= new Vector();
	_NWRDSO 	= new Vector();
	_NZERO 		= new Vector();
	_PCPPTR 	= new Vector();
	_PNTR 		= new Vector();
	_PRMPTR 	= new Vector();
	_RDATE 		= new Vector();
	_RPT2LG 	= new Vector();
	_RPTLG 		= new Vector();
	_SMNOZO 	= new Vector();
	_STAID 		= new Vector();
	_TMPPTR 	= new Vector();
	_TYPREC 	= -1;
}

public void setH8CREC(int H8CREC) {
	_H8CREC = H8CREC; 
}

public void setHINTRC(int HINTRC) {
	_HINTRC = HINTRC; 
}

public void setINFREC(int INFREC) {
	_INFREC = INFREC; 
}

public void setLFILE(int LFILE) {
	_LFILE = LFILE; 
}

public void setLRECL1(int LRECL1) {
	_LRECL1 = LRECL1; 
}

public void setLRECL2(int LRECL2) {
	_LRECL2 = LRECL2; 
}

public void setLRECL3(int LRECL3) {
	_LRECL3 = LRECL3; 
}

public void setLURRS(int LURRS) {
	_LURRS = LURRS; 
}

public void setMAXDDF(int MAXDDF) {
	_MAXDDF = MAXDDF; 
}

public void setMAXTYP(int MAXTYP) {
	_MAXTYP = MAXTYP; 
}

public void setMFILE(int MFILE) {
	_MFILE = MFILE; 
}

public void setNHASHR(int NHASHR) {
	_NHASHR = NHASHR; 
}

public void setNUMDDF(int NUMDDF) {
	_NUMDDF = NUMDDF; 
}

public void setNUMTYP(int NUMTYP) {
	_NUMTYP = NUMTYP; 
}

public void setNWRDS(int NWRDS) {
	_NWRDS = NWRDS; 
}

public void setTYPREC(int TYPREC) {
	_TYPREC = TYPREC; 
}

}
