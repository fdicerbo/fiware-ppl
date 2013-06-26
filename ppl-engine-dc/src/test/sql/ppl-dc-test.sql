-- phpMyAdmin SQL Dump
-- version 3.2.5
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Oct 19, 2010 at 10:56 AM
-- Server version: 5.1.43
-- PHP Version: 5.3.2

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `ppl-dc-test`
--
CREATE DATABASE `ppl-dc-test` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `ppl-dc-test`;

-- --------------------------------------------------------

--
-- Table structure for table `actionanonymizepersonaldata`
--

CREATE TABLE IF NOT EXISTS `actionanonymizepersonaldata` (
  `HJID` bigint(20) NOT NULL,
  `ACTIONANONYMIZEPERSONALDATA__0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKF97E99C84CEDE506` (`ACTIONANONYMIZEPERSONALDATA__0`),
  KEY `FKF97E99C89617EBDA` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `actionanonymizepersonaldata`
--


-- --------------------------------------------------------

--
-- Table structure for table `actionattributedesignatortype`
--

CREATE TABLE IF NOT EXISTS `actionattributedesignatortype` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK837EF7D46A5CA237` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `actionattributedesignatortype`
--


-- --------------------------------------------------------

--
-- Table structure for table `actiondeletepersonaldata`
--

CREATE TABLE IF NOT EXISTS `actiondeletepersonaldata` (
  `HJID` bigint(20) NOT NULL,
  `ACTIONDELETEPERSONALDATA_MIS_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK4C86BFCBD1F08A72` (`ACTIONDELETEPERSONALDATA_MIS_0`),
  KEY `FK4C86BFCB9617EBDA` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `actiondeletepersonaldata`
--


-- --------------------------------------------------------

--
-- Table structure for table `actionlog`
--

CREATE TABLE IF NOT EXISTS `actionlog` (
  `HJID` bigint(20) NOT NULL,
  `ACTIONLOG_MISMATCHPOLICY_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKB1EA012E28C91529` (`ACTIONLOG_MISMATCHPOLICY_HJID`),
  KEY `FKB1EA012E9617EBDA` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `actionlog`
--


-- --------------------------------------------------------

--
-- Table structure for table `actionmatchtype`
--

CREATE TABLE IF NOT EXISTS `actionmatchtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MATCHID` varchar(255) DEFAULT NULL,
  `ACTIONATTRIBUTEDESIGNATOR_AC_0` bigint(20) DEFAULT NULL,
  `ATTRIBUTESELECTOR_ACTIONMATC_0` bigint(20) DEFAULT NULL,
  `ATTRIBUTEVALUE_ACTIONMATCHTY_0` bigint(20) DEFAULT NULL,
  `ACTIONMATCH_ACTIONTYPE_0_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK8E1BB8E9C0271C7A` (`ATTRIBUTEVALUE_ACTIONMATCHTY_0`),
  KEY `FK8E1BB8E9B189F12B` (`ACTIONMATCH_ACTIONTYPE_0_HJID`),
  KEY `FK8E1BB8E935CDE03C` (`ACTIONATTRIBUTEDESIGNATOR_AC_0`),
  KEY `FK8E1BB8E910CD48D3` (`ATTRIBUTESELECTOR_ACTIONMATC_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `actionmatchtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `actionnotifydatasubject`
--

CREATE TABLE IF NOT EXISTS `actionnotifydatasubject` (
  `ADDRESS` varchar(255) DEFAULT NULL,
  `MEDIA` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  `ACTIONNOTIFYDATASUBJECT_MISM_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKE50A23C39617EBDA` (`HJID`),
  KEY `FKE50A23C3ADA3EA1` (`ACTIONNOTIFYDATASUBJECT_MISM_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `actionnotifydatasubject`
--


-- --------------------------------------------------------

--
-- Table structure for table `actionsecurelog`
--

CREATE TABLE IF NOT EXISTS `actionsecurelog` (
  `HJID` bigint(20) NOT NULL,
  `ACTIONSECURELOG_MISMATCHPOLI_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK432E3AD79617EBDA` (`HJID`),
  KEY `FK432E3AD7B45949A1` (`ACTIONSECURELOG_MISMATCHPOLI_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `actionsecurelog`
--


-- --------------------------------------------------------

--
-- Table structure for table `actionstype`
--

CREATE TABLE IF NOT EXISTS `actionstype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `actionstype`
--


-- --------------------------------------------------------

--
-- Table structure for table `actiontype`
--

CREATE TABLE IF NOT EXISTS `actiontype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAMESPACE` varchar(255) DEFAULT NULL,
  `VALUE_` varchar(255) DEFAULT NULL,
  `ACTION__AUTHZDECISIONSTATEME_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK8B59EE704D801008` (`ACTION__AUTHZDECISIONSTATEME_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `actiontype`
--


-- --------------------------------------------------------

--
-- Table structure for table `actiontype_0`
--

CREATE TABLE IF NOT EXISTS `actiontype_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACTION__ACTIONSTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK1C981E216A21BF02` (`ACTION__ACTIONSTYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `actiontype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `actiontype_1`
--

CREATE TABLE IF NOT EXISTS `actiontype_1` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `actiontype_1`
--


-- --------------------------------------------------------

--
-- Table structure for table `action_`
--

CREATE TABLE IF NOT EXISTS `action_` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ELEMENTID` varchar(255) DEFAULT NULL,
  `MATCHING` bit(1) DEFAULT NULL,
  `MISMATCHID` varchar(255) DEFAULT NULL,
  `NAME_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `action_`
--


-- --------------------------------------------------------

--
-- Table structure for table `advicetype`
--

CREATE TABLE IF NOT EXISTS `advicetype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `advicetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `advicetypeassertionidreforas_0`
--

CREATE TABLE IF NOT EXISTS `advicetypeassertionidreforas_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMASSERTIONIDREF` varchar(255) DEFAULT NULL,
  `ITEMASSERTIONURIREF` varchar(255) DEFAULT NULL,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `ITEMASSERTION_ADVICETYPEASSE_0` bigint(20) DEFAULT NULL,
  `ITEMENCRYPTEDASSERTION_ADVIC_0` bigint(20) DEFAULT NULL,
  `ASSERTIONIDREFORASSERTIONURI_1` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK52BCD64EFA41CB68` (`ITEMASSERTION_ADVICETYPEASSE_0`),
  KEY `FK52BCD64E8726E792` (`ASSERTIONIDREFORASSERTIONURI_1`),
  KEY `FK52BCD64E490641ED` (`ITEMENCRYPTEDASSERTION_ADVIC_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `advicetypeassertionidreforas_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `agreementmethodtype`
--

CREATE TABLE IF NOT EXISTS `agreementmethodtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ALGORITHM` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `agreementmethodtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `agreementmethodtypecontentit_0`
--

CREATE TABLE IF NOT EXISTS `agreementmethodtypecontentit_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMKANONCE` tinyblob,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `ITEMORIGINATORKEYINFO_AGREEM_0` bigint(20) DEFAULT NULL,
  `ITEMRECIPIENTKEYINFO_AGREEME_0` bigint(20) DEFAULT NULL,
  `CONTENTITEMS_AGREEMENTMETHOD_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKF82583D0EC41594` (`CONTENTITEMS_AGREEMENTMETHOD_0`),
  KEY `FKF82583D09BA98730` (`ITEMRECIPIENTKEYINFO_AGREEME_0`),
  KEY `FKF82583D0FAF43F06` (`ITEMORIGINATORKEYINFO_AGREEM_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `agreementmethodtypecontentit_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `applytype`
--

CREATE TABLE IF NOT EXISTS `applytype` (
  `FUNCTIONID` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK2A6EBB88A1AE81F` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `applytype`
--


-- --------------------------------------------------------

--
-- Table structure for table `applytypeexpressionitem`
--

CREATE TABLE IF NOT EXISTS `applytypeexpressionitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMNAME` varchar(255) DEFAULT NULL,
  `ITEMVALUE_APPLYTYPEEXPRESSIO_0` bigint(20) DEFAULT NULL,
  `EXPRESSIONITEMS_APPLYTYPE_HJ_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK8492D3333FBF6499` (`EXPRESSIONITEMS_APPLYTYPE_HJ_0`),
  KEY `FK8492D333A6AA4402` (`ITEMVALUE_APPLYTYPEEXPRESSIO_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `applytypeexpressionitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `assertiontype`
--

CREATE TABLE IF NOT EXISTS `assertiontype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID` varchar(255) DEFAULT NULL,
  `ISSUEINSTANTITEM` datetime DEFAULT NULL,
  `VERSION_` varchar(255) DEFAULT NULL,
  `ADVICE_ASSERTIONTYPE_HJID` bigint(20) DEFAULT NULL,
  `CONDITIONS_ASSERTIONTYPE_HJID` bigint(20) DEFAULT NULL,
  `ISSUER_ASSERTIONTYPE_HJID` bigint(20) DEFAULT NULL,
  `SIGNATURE_ASSERTIONTYPE_HJID` bigint(20) DEFAULT NULL,
  `SUBJECT_ASSERTIONTYPE_HJID` bigint(20) DEFAULT NULL,
  `ASSERTION__RESOURCEQUERYTYPE_0` bigint(20) DEFAULT NULL,
  `ASSERTION__CLAIMTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK60856D1C13481EA4` (`CONDITIONS_ASSERTIONTYPE_HJID`),
  KEY `FK60856D1C5CAA0424` (`ADVICE_ASSERTIONTYPE_HJID`),
  KEY `FK60856D1C4C755D74` (`ASSERTION__CLAIMTYPE_HJID`),
  KEY `FK60856D1C2D35E62E` (`SUBJECT_ASSERTIONTYPE_HJID`),
  KEY `FK60856D1C909875B1` (`ISSUER_ASSERTIONTYPE_HJID`),
  KEY `FK60856D1CD5C870AD` (`ASSERTION__RESOURCEQUERYTYPE_0`),
  KEY `FK60856D1CF698D745` (`SIGNATURE_ASSERTIONTYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `assertiontype`
--


-- --------------------------------------------------------

--
-- Table structure for table `assertiontypestatementorauth_0`
--

CREATE TABLE IF NOT EXISTS `assertiontypestatementorauth_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMATTRIBUTESTATEMENT_ASSER_0` bigint(20) DEFAULT NULL,
  `ITEMAUTHNSTATEMENT_ASSERTION_0` bigint(20) DEFAULT NULL,
  `ITEMAUTHZDECISIONSTATEMENT_A_0` bigint(20) DEFAULT NULL,
  `ITEMSTATEMENT_ASSERTIONTYPES_0` bigint(20) DEFAULT NULL,
  `STATEMENTORAUTHNSTATEMENTORA_1` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK13F753AFEEE369ED` (`ITEMAUTHZDECISIONSTATEMENT_A_0`),
  KEY `FK13F753AFA1C08D0B` (`ITEMSTATEMENT_ASSERTIONTYPES_0`),
  KEY `FK13F753AF75EFFE86` (`STATEMENTORAUTHNSTATEMENTORA_1`),
  KEY `FK13F753AFA9E3A73E` (`ITEMAUTHNSTATEMENT_ASSERTION_0`),
  KEY `FK13F753AFD036147E` (`ITEMATTRIBUTESTATEMENT_ASSER_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `assertiontypestatementorauth_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributeassignmenttype`
--

CREATE TABLE IF NOT EXISTS `attributeassignmenttype` (
  `ATTRIBUTEID` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  `ATTRIBUTEASSIGNMENT_OBLIGATI_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK3F967D037895AD4E` (`ATTRIBUTEASSIGNMENT_OBLIGATI_0`),
  KEY `FK3F967D03E648AF3C` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `attributeassignmenttype`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributedesignatortype`
--

CREATE TABLE IF NOT EXISTS `attributedesignatortype` (
  `ATTRIBUTEID` varchar(255) DEFAULT NULL,
  `DATATYPE` varchar(255) DEFAULT NULL,
  `ISSUER` varchar(255) DEFAULT NULL,
  `MUSTBEPRESENT` bit(1) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK168D86CAA1AE81F` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `attributedesignatortype`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributematchanyoftype`
--

CREATE TABLE IF NOT EXISTS `attributematchanyoftype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATTRIBUTEID` varchar(255) DEFAULT NULL,
  `DISCLOSE` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `attributematchanyoftype`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributematchanyoftypematch_0`
--

CREATE TABLE IF NOT EXISTS `attributematchanyoftypematch_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMMATCHVALUE_ATTRIBUTEMATC_0` bigint(20) DEFAULT NULL,
  `ITEMUNDISCLOSEDEXPRESSION_AT_0` bigint(20) DEFAULT NULL,
  `MATCHVALUEORUNDISCLOSEDEXPRE_1` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK9166C5C26E1B3AD6` (`ITEMMATCHVALUE_ATTRIBUTEMATC_0`),
  KEY `FK9166C5C24ACD8B64` (`MATCHVALUEORUNDISCLOSEDEXPRE_1`),
  KEY `FK9166C5C2E6D60A90` (`ITEMUNDISCLOSEDEXPRESSION_AT_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `attributematchanyoftypematch_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributeselectortype`
--

CREATE TABLE IF NOT EXISTS `attributeselectortype` (
  `DATATYPE` varchar(255) DEFAULT NULL,
  `MUSTBEPRESENT` bit(1) DEFAULT NULL,
  `REQUESTCONTEXTPATH` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK3E004415A1AE81F` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `attributeselectortype`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributestatementtype`
--

CREATE TABLE IF NOT EXISTS `attributestatementtype` (
  `EVIDENCEID` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK1977B00D7985B795` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `attributestatementtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributestatementtypeattrib_0`
--

CREATE TABLE IF NOT EXISTS `attributestatementtypeattrib_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMATTRIBUTE_ATTRIBUTESTATE_0` bigint(20) DEFAULT NULL,
  `ITEMENCRYPTEDATTRIBUTE_ATTRI_0` bigint(20) DEFAULT NULL,
  `ATTRIBUTEORENCRYPTEDATTRIBUT_1` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK95F5F708D6008B38` (`ATTRIBUTEORENCRYPTEDATTRIBUT_1`),
  KEY `FK95F5F708B4932452` (`ITEMENCRYPTEDATTRIBUTE_ATTRI_0`),
  KEY `FK95F5F708E3936027` (`ITEMATTRIBUTE_ATTRIBUTESTATE_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `attributestatementtypeattrib_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributestatementtype_0`
--

CREATE TABLE IF NOT EXISTS `attributestatementtype_0` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK9A4BEC7EF9AB83DD` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `attributestatementtype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributetype`
--

CREATE TABLE IF NOT EXISTS `attributetype` (
  `EVIDENCEID` varchar(255) DEFAULT NULL,
  `STICKYPOLICYID` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK636DA3B6D7492748` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `attributetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributetypeattributevaluei_0`
--

CREATE TABLE IF NOT EXISTS `attributetypeattributevaluei_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMOBJECT` longtext,
  `ATTRIBUTEVALUEITEMS_ATTRIBUT_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKA327B44F64E5E239` (`ATTRIBUTEVALUEITEMS_ATTRIBUT_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `attributetypeattributevaluei_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributetype_0`
--

CREATE TABLE IF NOT EXISTS `attributetype_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATTRIBUTEID` varchar(255) DEFAULT NULL,
  `DATATYPE` varchar(255) DEFAULT NULL,
  `ISSUER` varchar(255) DEFAULT NULL,
  `ATTRIBUTE__SUBJECTTYPE_0_HJID` bigint(20) DEFAULT NULL,
  `ATTRIBUTE__RESOURCETYPE_0_HJ_0` bigint(20) DEFAULT NULL,
  `ATTRIBUTE__ENVIRONMENTTYPE_H_0` bigint(20) DEFAULT NULL,
  `ATTRIBUTE__ACTIONTYPE_1_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK3E9399E7F8C247A9` (`ATTRIBUTE__ACTIONTYPE_1_HJID`),
  KEY `FK3E9399E73447D72C` (`ATTRIBUTE__SUBJECTTYPE_0_HJID`),
  KEY `FK3E9399E7B63246E0` (`ATTRIBUTE__RESOURCETYPE_0_HJ_0`),
  KEY `FK3E9399E79348F6FD` (`ATTRIBUTE__ENVIRONMENTTYPE_H_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `attributetype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributetype_1`
--

CREATE TABLE IF NOT EXISTS `attributetype_1` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID` varchar(255) DEFAULT NULL,
  `ATTRIBUTEURI` varchar(255) DEFAULT NULL,
  `MATCHING` bit(1) DEFAULT NULL,
  `AUTHORIZATIONSSET_ATTRIBUTET_0` bigint(20) DEFAULT NULL,
  `MISMATCHES_ATTRIBUTETYPE_1_H_0` bigint(20) DEFAULT NULL,
  `OBLIGATIONSSET_ATTRIBUTETYPE_0` bigint(20) DEFAULT NULL,
  `ATTRIBUTE__STICKYPOLICY_HJID` bigint(20) DEFAULT NULL,
  `ATTRIBUTE__STICKYPOLICYSTATE_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK3E9399E854C9650F` (`ATTRIBUTE__STICKYPOLICYSTATE_0`),
  KEY `FK3E9399E82D1BB6EE` (`ATTRIBUTE__STICKYPOLICY_HJID`),
  KEY `FK3E9399E8F03F2DC9` (`OBLIGATIONSSET_ATTRIBUTETYPE_0`),
  KEY `FK3E9399E88EEF6FFD` (`AUTHORIZATIONSSET_ATTRIBUTET_0`),
  KEY `FK3E9399E8A0423DE3` (`MISMATCHES_ATTRIBUTETYPE_1_H_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `attributetype_1`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributetype_2`
--

CREATE TABLE IF NOT EXISTS `attributetype_2` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FRIENDLYNAME` varchar(255) DEFAULT NULL,
  `NAME_` varchar(255) DEFAULT NULL,
  `NAMEFORMAT` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `attributetype_2`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributevaluetype`
--

CREATE TABLE IF NOT EXISTS `attributevaluetype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATTRIBUTEVALUE_MISSINGATTRIB_0` bigint(20) DEFAULT NULL,
  `ATTRIBUTEVALUE_ATTRIBUTETYPE_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKCC47610FAFBFBCB8` (`ATTRIBUTEVALUE_ATTRIBUTETYPE_0`),
  KEY `FKCC47610F30FA5493` (`ATTRIBUTEVALUE_MISSINGATTRIB_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `attributevaluetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributevaluetypecontentitem`
--

CREATE TABLE IF NOT EXISTS `attributevaluetypecontentitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `CONTENTITEMS_ATTRIBUTEVALUET_1` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKBD960E7D4F453CF8` (`CONTENTITEMS_ATTRIBUTEVALUET_1`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `attributevaluetypecontentitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributevaluetypecontentite_0`
--

CREATE TABLE IF NOT EXISTS `attributevaluetypecontentite_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `CONTENTITEMS_ATTRIBUTEVALUET_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKF52BC381D87558E2` (`CONTENTITEMS_ATTRIBUTEVALUET_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `attributevaluetypecontentite_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `attributevaluetype_0`
--

CREATE TABLE IF NOT EXISTS `attributevaluetype_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATATYPE` varchar(255) DEFAULT NULL,
  `ATTRIBUTEVALUE_PROVISIONALAC_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKD7F3650039BCD874` (`ATTRIBUTEVALUE_PROVISIONALAC_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `attributevaluetype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `audiencerestrictiontype`
--

CREATE TABLE IF NOT EXISTS `audiencerestrictiontype` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK85A6D662F259A589` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `audiencerestrictiontype`
--


-- --------------------------------------------------------

--
-- Table structure for table `audiencerestrictiontypeaudie_0`
--

CREATE TABLE IF NOT EXISTS `audiencerestrictiontypeaudie_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEM` varchar(255) DEFAULT NULL,
  `AUDIENCEITEMS_AUDIENCERESTRI_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKF6B10BBBADAFDF9A` (`AUDIENCEITEMS_AUDIENCERESTRI_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `audiencerestrictiontypeaudie_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `authncontexttype`
--

CREATE TABLE IF NOT EXISTS `authncontexttype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `authncontexttype`
--


-- --------------------------------------------------------

--
-- Table structure for table `authncontexttypecontentitem`
--

CREATE TABLE IF NOT EXISTS `authncontexttypecontentitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMAUTHENTICATINGAUTHORITY` varchar(255) DEFAULT NULL,
  `ITEMAUTHNCONTEXTCLASSREF` varchar(255) DEFAULT NULL,
  `ITEMAUTHNCONTEXTDECLREF` varchar(255) DEFAULT NULL,
  `CONTENTITEMS_AUTHNCONTEXTTYP_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKEE3AFD29E75DEEA9` (`CONTENTITEMS_AUTHNCONTEXTTYP_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `authncontexttypecontentitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `authnstatementtype`
--

CREATE TABLE IF NOT EXISTS `authnstatementtype` (
  `AUTHNINSTANTITEM` datetime DEFAULT NULL,
  `SESSIONINDEX` varchar(255) DEFAULT NULL,
  `SESSIONNOTONORAFTERITEM` datetime DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  `AUTHNCONTEXT_AUTHNSTATEMENTT_0` bigint(20) DEFAULT NULL,
  `SUBJECTLOCALITY_AUTHNSTATEME_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK9EA2BF23FA8E2E82` (`SUBJECTLOCALITY_AUTHNSTATEME_0`),
  KEY `FK9EA2BF23F9AB83DD` (`HJID`),
  KEY `FK9EA2BF231A1FD294` (`AUTHNCONTEXT_AUTHNSTATEMENTT_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `authnstatementtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `authorizationsmismatchtype`
--

CREATE TABLE IF NOT EXISTS `authorizationsmismatchtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MISMATCHID` varchar(255) DEFAULT NULL,
  `AUTHORIZATIONSSET_AUTHORIZAT_0` bigint(20) DEFAULT NULL,
  `AUTHZDOWNSTREAMUSAGE_AUTHORI_0` bigint(20) DEFAULT NULL,
  `AUTHZUSEFORPURPOSE_AUTHORIZA_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK2A56AE22138E6A8C` (`AUTHZUSEFORPURPOSE_AUTHORIZA_0`),
  KEY `FK2A56AE22A2B20E` (`AUTHORIZATIONSSET_AUTHORIZAT_0`),
  KEY `FK2A56AE22907F944B` (`AUTHZDOWNSTREAMUSAGE_AUTHORI_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `authorizationsmismatchtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `authorizationssetmismatchtype`
--

CREATE TABLE IF NOT EXISTS `authorizationssetmismatchtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MISMATCHID` varchar(255) DEFAULT NULL,
  `POLICY_AUTHORIZATIONSSETMISM_0` bigint(20) DEFAULT NULL,
  `PREFERENCE_AUTHORIZATIONSSET_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKFFAF74F0F81AB13F` (`POLICY_AUTHORIZATIONSSETMISM_0`),
  KEY `FKFFAF74F088C05072` (`PREFERENCE_AUTHORIZATIONSSET_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `authorizationssetmismatchtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `authorizationssettype`
--

CREATE TABLE IF NOT EXISTS `authorizationssettype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MATCHING` bit(1) DEFAULT NULL,
  `MISMATCHIDOBJECT` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `authorizationssettype`
--


-- --------------------------------------------------------

--
-- Table structure for table `authorizationssettypeauthori_0`
--

CREATE TABLE IF NOT EXISTS `authorizationssettypeauthori_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMNAME` varchar(255) DEFAULT NULL,
  `ITEMVALUE_AUTHORIZATIONSSETT_0` bigint(20) DEFAULT NULL,
  `AUTHORIZATIONITEMS_AUTHORIZA_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK80D66C6D4391EE2C` (`ITEMVALUE_AUTHORIZATIONSSETT_0`),
  KEY `FK80D66C6D19FEE1B` (`AUTHORIZATIONITEMS_AUTHORIZA_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `authorizationssettypeauthori_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `authorizationtype`
--

CREATE TABLE IF NOT EXISTS `authorizationtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MATCHING` bit(1) DEFAULT NULL,
  `MISMATCHIDOBJECT` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `authorizationtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `authorizationtypeanyitem`
--

CREATE TABLE IF NOT EXISTS `authorizationtypeanyitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `ANYITEMS_AUTHORIZATIONTYPE_H_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK45ADF46CB246E10` (`ANYITEMS_AUTHORIZATIONTYPE_H_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `authorizationtypeanyitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `authzdecisionstatementtype`
--

CREATE TABLE IF NOT EXISTS `authzdecisionstatementtype` (
  `DECISION` varchar(255) DEFAULT NULL,
  `RESOURCE_` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  `EVIDENCE_AUTHZDECISIONSTATEM_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKB6224A3BC49C944F` (`EVIDENCE_AUTHZDECISIONSTATEM_0`),
  KEY `FKB6224A3BF9AB83DD` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `authzdecisionstatementtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `authzdownstreamusage`
--

CREATE TABLE IF NOT EXISTS `authzdownstreamusage` (
  `ALLOWED` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK7182022D28D6494B` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `authzdownstreamusage`
--


-- --------------------------------------------------------

--
-- Table structure for table `authzdownstreamusagemismatch_0`
--

CREATE TABLE IF NOT EXISTS `authzdownstreamusagemismatch_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MISMATCHID` varchar(255) DEFAULT NULL,
  `POLICY_AUTHZDOWNSTREAMUSAGEM_0` bigint(20) DEFAULT NULL,
  `PREFERENCE_AUTHZDOWNSTREAMUS_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKB9213FEC3EFF330D` (`PREFERENCE_AUTHZDOWNSTREAMUS_0`),
  KEY `FKB9213FEC219FCB32` (`POLICY_AUTHZDOWNSTREAMUSAGEM_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `authzdownstreamusagemismatch_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `authzuseforpurpose`
--

CREATE TABLE IF NOT EXISTS `authzuseforpurpose` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK25B5BE6A28D6494B` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `authzuseforpurpose`
--


-- --------------------------------------------------------

--
-- Table structure for table `authzuseforpurposemismatchty_0`
--

CREATE TABLE IF NOT EXISTS `authzuseforpurposemismatchty_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MISMATCHID` varchar(255) DEFAULT NULL,
  `POLICY_AUTHZUSEFORPURPOSEMIS_0` bigint(20) DEFAULT NULL,
  `PREFERENCE_AUTHZUSEFORPURPOS_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK37EE32E428B8D45` (`PREFERENCE_AUTHZUSEFORPURPOS_0`),
  KEY `FK37EE32E822106E` (`POLICY_AUTHZUSEFORPURPOSEMIS_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `authzuseforpurposemismatchty_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `baseidabstracttype`
--

CREATE TABLE IF NOT EXISTS `baseidabstracttype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SPNAMEQUALIFIER` varchar(255) DEFAULT NULL,
  `NAMEQUALIFIER` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `baseidabstracttype`
--


-- --------------------------------------------------------

--
-- Table structure for table `canonicalizationmethodtype`
--

CREATE TABLE IF NOT EXISTS `canonicalizationmethodtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ALGORITHM` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `canonicalizationmethodtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `canonicalizationmethodtypeco_0`
--

CREATE TABLE IF NOT EXISTS `canonicalizationmethodtypeco_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `CONTENTITEMS_CANONICALIZATIO_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKDDA93828A75536D6` (`CONTENTITEMS_CANONICALIZATIO_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `canonicalizationmethodtypeco_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `cipherdatatype`
--

CREATE TABLE IF NOT EXISTS `cipherdatatype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CIPHERVALUE` longblob,
  `CIPHERREFERENCE_CIPHERDATATY_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK574D28EF62980B2F` (`CIPHERREFERENCE_CIPHERDATATY_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `cipherdatatype`
--


-- --------------------------------------------------------

--
-- Table structure for table `cipherreferencetype`
--

CREATE TABLE IF NOT EXISTS `cipherreferencetype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `URI` varchar(255) DEFAULT NULL,
  `TRANSFORMS__CIPHERREFERENCET_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK31B458BAA61A2C2` (`TRANSFORMS__CIPHERREFERENCET_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `cipherreferencetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `claimstype`
--

CREATE TABLE IF NOT EXISTS `claimstype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `claimstype`
--


-- --------------------------------------------------------

--
-- Table structure for table `claimtype`
--

CREATE TABLE IF NOT EXISTS `claimtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CLAIM_CLAIMSTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK942DC9561DC71BC3` (`CLAIM_CLAIMSTYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `claimtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `combinerparameterstype`
--

CREATE TABLE IF NOT EXISTS `combinerparameterstype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `combinerparameterstype`
--


-- --------------------------------------------------------

--
-- Table structure for table `combinerparametertype`
--

CREATE TABLE IF NOT EXISTS `combinerparametertype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PARAMETERNAME` varchar(255) DEFAULT NULL,
  `ATTRIBUTEVALUE_COMBINERPARAM_0` bigint(20) DEFAULT NULL,
  `COMBINERPARAMETER_COMBINERPA_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK40149A10FCA58D65` (`COMBINERPARAMETER_COMBINERPA_0`),
  KEY `FK40149A103EC88040` (`ATTRIBUTEVALUE_COMBINERPARAM_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `combinerparametertype`
--


-- --------------------------------------------------------

--
-- Table structure for table `commondhpsptype`
--

CREATE TABLE IF NOT EXISTS `commondhpsptype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `AUTHORIZATIONSSET_COMMONDHPS_0` bigint(20) DEFAULT NULL,
  `OBLIGATIONSSET_COMMONDHPSPTY_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK5A4F14D8BFAAA76` (`OBLIGATIONSSET_COMMONDHPSPTY_0`),
  KEY `FK5A4F14D8EEEE4E17` (`AUTHORIZATIONSSET_COMMONDHPS_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `commondhpsptype`
--


-- --------------------------------------------------------

--
-- Table structure for table `conditionabstracttype`
--

CREATE TABLE IF NOT EXISTS `conditionabstracttype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `conditionabstracttype`
--


-- --------------------------------------------------------

--
-- Table structure for table `conditionstatementtype`
--

CREATE TABLE IF NOT EXISTS `conditionstatementtype` (
  `EVIDENCEID` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  `CONDITION_CONDITIONSTATEMENT_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK830833EEB59E126E` (`CONDITION_CONDITIONSTATEMENT_0`),
  KEY `FK830833EEF9AB83DD` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `conditionstatementtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `conditionstype`
--

CREATE TABLE IF NOT EXISTS `conditionstype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NOTBEFOREITEM` datetime DEFAULT NULL,
  `NOTONORAFTERITEM` datetime DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `conditionstype`
--


-- --------------------------------------------------------

--
-- Table structure for table `conditionstypeconditionoraud_0`
--

CREATE TABLE IF NOT EXISTS `conditionstypeconditionoraud_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMAUDIENCERESTRICTION_COND_0` bigint(20) DEFAULT NULL,
  `ITEMCONDITION_CONDITIONSTYPE_0` bigint(20) DEFAULT NULL,
  `ITEMONETIMEUSE_CONDITIONSTYP_0` bigint(20) DEFAULT NULL,
  `ITEMPROXYRESTRICTION_CONDITI_0` bigint(20) DEFAULT NULL,
  `CONDITIONORAUDIENCERESTRICTI_1` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKDE06BED5D5A32D06` (`ITEMCONDITION_CONDITIONSTYPE_0`),
  KEY `FKDE06BED5F839F362` (`ITEMPROXYRESTRICTION_CONDITI_0`),
  KEY `FKDE06BED59A4381C5` (`ITEMONETIMEUSE_CONDITIONSTYP_0`),
  KEY `FKDE06BED56E1A6E74` (`ITEMAUDIENCERESTRICTION_COND_0`),
  KEY `FKDE06BED5441C571A` (`CONDITIONORAUDIENCERESTRICTI_1`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `conditionstypeconditionoraud_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `conditiontype`
--

CREATE TABLE IF NOT EXISTS `conditiontype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EXPRESSIONNAME` varchar(255) DEFAULT NULL,
  `EXPRESSIONVALUE_CONDITIONTYP_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKB9C0EB5C869ECE9` (`EXPRESSIONVALUE_CONDITIONTYP_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `conditiontype`
--


-- --------------------------------------------------------

--
-- Table structure for table `conditiontype_0`
--

CREATE TABLE IF NOT EXISTS `conditiontype_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EXPRESSIONNAME` varchar(255) DEFAULT NULL,
  `EXPRESSIONVALUE_CONDITIONTYP_1` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK94D34126C869ECEA` (`EXPRESSIONVALUE_CONDITIONTYP_1`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `conditiontype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `credentialattributedesignato_0`
--

CREATE TABLE IF NOT EXISTS `credentialattributedesignato_0` (
  `ATTRIBUTEID` varchar(255) DEFAULT NULL,
  `CREDENTIALID` varchar(255) DEFAULT NULL,
  `DATATYPE` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKCD8BEF0AA1AE81F` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `credentialattributedesignato_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `credentialrequirementstype`
--

CREATE TABLE IF NOT EXISTS `credentialrequirementstype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CONDITION_CREDENTIALREQUIREM_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK367C9A217652E329` (`CONDITION_CREDENTIALREQUIREM_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `credentialrequirementstype`
--


-- --------------------------------------------------------

--
-- Table structure for table `credentialtype`
--

CREATE TABLE IF NOT EXISTS `credentialtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `CREDENTIALID` varchar(255) DEFAULT NULL,
  `CREDENTIAL_CREDENTIALREQUIRE_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKC615581178439938` (`CREDENTIAL_CREDENTIALREQUIRE_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `credentialtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `credentialtypeattributematch_0`
--

CREATE TABLE IF NOT EXISTS `credentialtypeattributematch_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMATTRIBUTEMATCHANYOF_CRED_0` bigint(20) DEFAULT NULL,
  `ITEMUNDISCLOSEDEXPRESSION_CR_0` bigint(20) DEFAULT NULL,
  `ATTRIBUTEMATCHANYOFORUNDISCL_1` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKB483F02BE6D6EBCC` (`ITEMUNDISCLOSEDEXPRESSION_CR_0`),
  KEY `FKB483F02B85BB23C4` (`ATTRIBUTEMATCHANYOFORUNDISCL_1`),
  KEY `FKB483F02B9D17A7CB` (`ITEMATTRIBUTEMATCHANYOF_CRED_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `credentialtypeattributematch_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `datahandlingpolicytype`
--

CREATE TABLE IF NOT EXISTS `datahandlingpolicytype` (
  `POLICYID` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  `DATAHANDLINGPOLICY_RULETYPE__0` bigint(20) DEFAULT NULL,
  `DATAHANDLINGPOLICY_POLICYTYP_0` bigint(20) DEFAULT NULL,
  `DATAHANDLINGPOLICY_POLICYSET_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKB552BF3BEDAFCDE8` (`DATAHANDLINGPOLICY_POLICYSET_0`),
  KEY `FKB552BF3BA45D4F37` (`DATAHANDLINGPOLICY_RULETYPE__0`),
  KEY `FKB552BF3B4679825D` (`DATAHANDLINGPOLICY_POLICYTYP_0`),
  KEY `FKB552BF3BFEFFA9B0` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `datahandlingpolicytype`
--


-- --------------------------------------------------------

--
-- Table structure for table `datahandlingpreferencestype`
--

CREATE TABLE IF NOT EXISTS `datahandlingpreferencestype` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKD349CFA3FEFFA9B0` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `datahandlingpreferencestype`
--


-- --------------------------------------------------------

--
-- Table structure for table `dateandtime`
--

CREATE TABLE IF NOT EXISTS `dateandtime` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATEANDTIMEITEM` datetime DEFAULT NULL,
  `ELEMENTID` varchar(255) DEFAULT NULL,
  `MATCHING` bit(1) DEFAULT NULL,
  `MISMATCHID` varchar(255) DEFAULT NULL,
  `STARTNOWOBJECT` longtext,
  `DATEANDTIME_MISMATCHPOLICY_H_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK30FF4D96FAC4FEA1` (`DATEANDTIME_MISMATCHPOLICY_H_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `dateandtime`
--


-- --------------------------------------------------------

--
-- Table structure for table `dcresource`
--

CREATE TABLE IF NOT EXISTS `dcresource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `policy_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  KEY `FKAED2E18D553F739B` (`policy_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `dcresource`
--


-- --------------------------------------------------------

--
-- Table structure for table `defaultstype`
--

CREATE TABLE IF NOT EXISTS `defaultstype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `XPATHVERSION` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `defaultstype`
--


-- --------------------------------------------------------

--
-- Table structure for table `digestmethodtype`
--

CREATE TABLE IF NOT EXISTS `digestmethodtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ALGORITHM` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `digestmethodtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `digestmethodtypecontentitem`
--

CREATE TABLE IF NOT EXISTS `digestmethodtypecontentitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `CONTENTITEMS_DIGESTMETHODTYP_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK5C1B9D2D323D2B32` (`CONTENTITEMS_DIGESTMETHODTYP_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `digestmethodtypecontentitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `dsakeyvaluetype`
--

CREATE TABLE IF NOT EXISTS `dsakeyvaluetype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `G_` longblob,
  `J` longblob,
  `P` longblob,
  `PGENCOUNTER` longblob,
  `Q` longblob,
  `SEED` longblob,
  `Y` longblob,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `dsakeyvaluetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `duration`
--

CREATE TABLE IF NOT EXISTS `duration` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DURATION` varchar(255) DEFAULT NULL,
  `ELEMENTID` varchar(255) DEFAULT NULL,
  `MATCHING` bit(1) DEFAULT NULL,
  `MISMATCHID` varchar(255) DEFAULT NULL,
  `DURATION_MISMATCHPOLICY_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKB7EA3D94E348926F` (`DURATION_MISMATCHPOLICY_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `duration`
--


-- --------------------------------------------------------

--
-- Table structure for table `encrypteddatatype`
--

CREATE TABLE IF NOT EXISTS `encrypteddatatype` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK571DEF889E6B0426` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `encrypteddatatype`
--


-- --------------------------------------------------------

--
-- Table structure for table `encryptedelementtype`
--

CREATE TABLE IF NOT EXISTS `encryptedelementtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ENCRYPTEDDATA_ENCRYPTEDELEME_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKFB3B6BF26B6C4527` (`ENCRYPTEDDATA_ENCRYPTEDELEME_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `encryptedelementtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `encryptedkeytype`
--

CREATE TABLE IF NOT EXISTS `encryptedkeytype` (
  `CARRIEDKEYNAME` varchar(255) DEFAULT NULL,
  `RECIPIENT` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  `REFERENCELIST_ENCRYPTEDKEYTY_0` bigint(20) DEFAULT NULL,
  `ENCRYPTEDKEY_ENCRYPTEDELEMEN_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK9503C7D572A3229B` (`REFERENCELIST_ENCRYPTEDKEYTY_0`),
  KEY `FK9503C7D5F3300F66` (`ENCRYPTEDKEY_ENCRYPTEDELEMEN_0`),
  KEY `FK9503C7D59E6B0426` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `encryptedkeytype`
--


-- --------------------------------------------------------

--
-- Table structure for table `encryptedtype`
--

CREATE TABLE IF NOT EXISTS `encryptedtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ENCODING_` varchar(255) DEFAULT NULL,
  `ID` varchar(255) DEFAULT NULL,
  `MIMETYPE` varchar(255) DEFAULT NULL,
  `TYPE_` varchar(255) DEFAULT NULL,
  `CIPHERDATA_ENCRYPTEDTYPE_HJID` bigint(20) DEFAULT NULL,
  `ENCRYPTIONMETHOD_ENCRYPTEDTY_0` bigint(20) DEFAULT NULL,
  `ENCRYPTIONPROPERTIES_ENCRYPT_0` bigint(20) DEFAULT NULL,
  `KEYINFO_ENCRYPTEDTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKB02A5EDE35035DF2` (`ENCRYPTIONPROPERTIES_ENCRYPT_0`),
  KEY `FKB02A5EDEF86B143` (`KEYINFO_ENCRYPTEDTYPE_HJID`),
  KEY `FKB02A5EDE3AF875AC` (`CIPHERDATA_ENCRYPTEDTYPE_HJID`),
  KEY `FKB02A5EDE7D0D13B2` (`ENCRYPTIONMETHOD_ENCRYPTEDTY_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `encryptedtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `encryptionmethodtype`
--

CREATE TABLE IF NOT EXISTS `encryptionmethodtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ALGORITHM` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `encryptionmethodtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `encryptionmethodtypecontenti_0`
--

CREATE TABLE IF NOT EXISTS `encryptionmethodtypecontenti_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMKEYSIZE` decimal(19,2) DEFAULT NULL,
  `ITEMOAEPPARAMS` tinyblob,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `CONTENTITEMS_ENCRYPTIONMETHO_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKD06F5C9F1A43206C` (`CONTENTITEMS_ENCRYPTIONMETHO_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `encryptionmethodtypecontenti_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `encryptionpropertiestype`
--

CREATE TABLE IF NOT EXISTS `encryptionpropertiestype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `encryptionpropertiestype`
--


-- --------------------------------------------------------

--
-- Table structure for table `encryptionpropertytype`
--

CREATE TABLE IF NOT EXISTS `encryptionpropertytype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID` varchar(255) DEFAULT NULL,
  `TARGET` varchar(255) DEFAULT NULL,
  `ENCRYPTIONPROPERTY_ENCRYPTIO_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK7DD3319219E744FA` (`ENCRYPTIONPROPERTY_ENCRYPTIO_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `encryptionpropertytype`
--


-- --------------------------------------------------------

--
-- Table structure for table `encryptionpropertytypeconten_0`
--

CREATE TABLE IF NOT EXISTS `encryptionpropertytypeconten_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `CONTENTITEMS_ENCRYPTIONPROPE_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKAFEECBDE9CA3271F` (`CONTENTITEMS_ENCRYPTIONPROPE_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `encryptionpropertytypeconten_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `environmentattributedesignat_2`
--

CREATE TABLE IF NOT EXISTS `environmentattributedesignat_2` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKD8D5E08D6A5CA237` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `environmentattributedesignat_2`
--


-- --------------------------------------------------------

--
-- Table structure for table `environmentmatchtype`
--

CREATE TABLE IF NOT EXISTS `environmentmatchtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MATCHID` varchar(255) DEFAULT NULL,
  `ATTRIBUTESELECTOR_ENVIRONMEN_0` bigint(20) DEFAULT NULL,
  `ATTRIBUTEVALUE_ENVIRONMENTMA_0` bigint(20) DEFAULT NULL,
  `ENVIRONMENTATTRIBUTEDESIGNAT_1` bigint(20) DEFAULT NULL,
  `ENVIRONMENTMATCH_ENVIRONMENT_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKFCF49CCECDB7B1B` (`ATTRIBUTESELECTOR_ENVIRONMEN_0`),
  KEY `FKFCF49CCD8D9F68D` (`ATTRIBUTEVALUE_ENVIRONMENTMA_0`),
  KEY `FKFCF49CC73C3474` (`ENVIRONMENTMATCH_ENVIRONMENT_0`),
  KEY `FKFCF49CC9CF5C8B3` (`ENVIRONMENTATTRIBUTEDESIGNAT_1`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `environmentmatchtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `environmentstype`
--

CREATE TABLE IF NOT EXISTS `environmentstype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `environmentstype`
--


-- --------------------------------------------------------

--
-- Table structure for table `environmenttype`
--

CREATE TABLE IF NOT EXISTS `environmenttype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `environmenttype`
--


-- --------------------------------------------------------

--
-- Table structure for table `environmenttype_0`
--

CREATE TABLE IF NOT EXISTS `environmenttype_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ENVIRONMENT_ENVIRONMENTSTYPE_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK3AB9F01E265B3F81` (`ENVIRONMENT_ENVIRONMENTSTYPE_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `environmenttype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `evidencestatementtype`
--

CREATE TABLE IF NOT EXISTS `evidencestatementtype` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKC1683812F9AB83DD` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `evidencestatementtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `evidencetype`
--

CREATE TABLE IF NOT EXISTS `evidencetype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `evidencetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `evidencetypeanyitem`
--

CREATE TABLE IF NOT EXISTS `evidencetypeanyitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `ANYITEMS_EVIDENCETYPE_0_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK23F53AE17354F0A` (`ANYITEMS_EVIDENCETYPE_0_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `evidencetypeanyitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `evidencetypeassertionidrefor_0`
--

CREATE TABLE IF NOT EXISTS `evidencetypeassertionidrefor_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMASSERTIONIDREF` varchar(255) DEFAULT NULL,
  `ITEMASSERTIONURIREF` varchar(255) DEFAULT NULL,
  `ITEMASSERTION_EVIDENCETYPEAS_0` bigint(20) DEFAULT NULL,
  `ITEMENCRYPTEDASSERTION_EVIDE_0` bigint(20) DEFAULT NULL,
  `ASSERTIONIDREFORASSERTIONURI_2` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKE9250D7B8A584F5` (`ITEMASSERTION_EVIDENCETYPEAS_0`),
  KEY `FKE9250D7B3A9CFA39` (`ITEMENCRYPTEDASSERTION_EVIDE_0`),
  KEY `FKE9250D7B65D052B2` (`ASSERTIONIDREFORASSERTIONURI_2`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `evidencetypeassertionidrefor_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `evidencetype_0`
--

CREATE TABLE IF NOT EXISTS `evidencetype_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID` varchar(255) DEFAULT NULL,
  `EVIDENCE_EVIDENCESTATEMENTTY_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKCA37DD821324D36E` (`EVIDENCE_EVIDENCESTATEMENTTY_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `evidencetype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `expressiontype`
--

CREATE TABLE IF NOT EXISTS `expressiontype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `expressiontype`
--


-- --------------------------------------------------------

--
-- Table structure for table `functiontype`
--

CREATE TABLE IF NOT EXISTS `functiontype` (
  `FUNCTIONID` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK7D973F12A1AE81F` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `functiontype`
--


-- --------------------------------------------------------

--
-- Table structure for table `idreferencetype`
--

CREATE TABLE IF NOT EXISTS `idreferencetype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EARLIESTVERSION` varchar(255) DEFAULT NULL,
  `LATESTVERSION` varchar(255) DEFAULT NULL,
  `VALUE_` varchar(255) DEFAULT NULL,
  `VERSION_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `idreferencetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `keyinfoconfirmationdatatype`
--

CREATE TABLE IF NOT EXISTS `keyinfoconfirmationdatatype` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK2A7D480620E5BB37` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `keyinfoconfirmationdatatype`
--


-- --------------------------------------------------------

--
-- Table structure for table `keyinfotype`
--

CREATE TABLE IF NOT EXISTS `keyinfotype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `keyinfotype`
--


-- --------------------------------------------------------

--
-- Table structure for table `keyinfotypecontentitem`
--

CREATE TABLE IF NOT EXISTS `keyinfotypecontentitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMKEYNAME` varchar(255) DEFAULT NULL,
  `ITEMMGMTDATA` varchar(255) DEFAULT NULL,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `ITEMKEYVALUE_KEYINFOTYPECONT_0` bigint(20) DEFAULT NULL,
  `ITEMPGPDATA_KEYINFOTYPECONTE_0` bigint(20) DEFAULT NULL,
  `ITEMRETRIEVALMETHOD_KEYINFOT_0` bigint(20) DEFAULT NULL,
  `ITEMSPKIDATA_KEYINFOTYPECONT_0` bigint(20) DEFAULT NULL,
  `ITEMX509DATA_KEYINFOTYPECONT_0` bigint(20) DEFAULT NULL,
  `CONTENTITEMS_KEYINFOTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK91434065A715FA00` (`CONTENTITEMS_KEYINFOTYPE_HJID`),
  KEY `FK91434065ADF1C90C` (`ITEMX509DATA_KEYINFOTYPECONT_0`),
  KEY `FK91434065668C7D0` (`ITEMKEYVALUE_KEYINFOTYPECONT_0`),
  KEY `FK91434065AF2A1016` (`ITEMSPKIDATA_KEYINFOTYPECONT_0`),
  KEY `FK914340658C62D9D0` (`ITEMRETRIEVALMETHOD_KEYINFOT_0`),
  KEY `FK91434065DEEBA1D5` (`ITEMPGPDATA_KEYINFOTYPECONTE_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `keyinfotypecontentitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `keyvaluetype`
--

CREATE TABLE IF NOT EXISTS `keyvaluetype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `keyvaluetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `keyvaluetypecontentitem`
--

CREATE TABLE IF NOT EXISTS `keyvaluetypecontentitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `ITEMDSAKEYVALUE_KEYVALUETYPE_0` bigint(20) DEFAULT NULL,
  `ITEMRSAKEYVALUE_KEYVALUETYPE_0` bigint(20) DEFAULT NULL,
  `CONTENTITEMS_KEYVALUETYPE_HJ_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK5F80F6C01BDC0035` (`ITEMDSAKEYVALUE_KEYVALUETYPE_0`),
  KEY `FK5F80F6C08D3FA22E` (`CONTENTITEMS_KEYVALUETYPE_HJ_0`),
  KEY `FK5F80F6C01FBE5B75` (`ITEMRSAKEYVALUE_KEYVALUETYPE_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `keyvaluetypecontentitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `listpiitype`
--

CREATE TABLE IF NOT EXISTS `listpiitype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `listpiitype`
--


-- --------------------------------------------------------

--
-- Table structure for table `listpiitypevalueitem`
--

CREATE TABLE IF NOT EXISTS `listpiitypevalueitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEM` varchar(255) DEFAULT NULL,
  `VALUEITEMS_LISTPIITYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKE991C318C33C3802` (`VALUEITEMS_LISTPIITYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `listpiitypevalueitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `manifesttype`
--

CREATE TABLE IF NOT EXISTS `manifesttype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `manifesttype`
--


-- --------------------------------------------------------

--
-- Table structure for table `matchvaluetype`
--

CREATE TABLE IF NOT EXISTS `matchvaluetype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATATYPE` varchar(255) DEFAULT NULL,
  `DISCLOSE` varchar(255) DEFAULT NULL,
  `MATCHID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `matchvaluetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `matchvaluetypecontentitem`
--

CREATE TABLE IF NOT EXISTS `matchvaluetypecontentitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `CONTENTITEMS_MATCHVALUETYPE__0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKD6F80726F8A3E098` (`CONTENTITEMS_MATCHVALUETYPE__0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `matchvaluetypecontentitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `mismatch`
--

CREATE TABLE IF NOT EXISTS `mismatch` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MISMATCHID` varchar(255) DEFAULT NULL,
  `SIMILARITY` double DEFAULT NULL,
  `POLICY_MISMATCH_HJID` bigint(20) DEFAULT NULL,
  `PREFERENCE_MISMATCH_HJID` bigint(20) DEFAULT NULL,
  `MISMATCH_MISMATCHES_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKE65F81CEC2EC70A8` (`PREFERENCE_MISMATCH_HJID`),
  KEY `FKE65F81CE30E1C589` (`MISMATCH_MISMATCHES_HJID`),
  KEY `FKE65F81CEBD3874BF` (`POLICY_MISMATCH_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `mismatch`
--


-- --------------------------------------------------------

--
-- Table structure for table `mismatches`
--

CREATE TABLE IF NOT EXISTS `mismatches` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `mismatches`
--


-- --------------------------------------------------------

--
-- Table structure for table `mismatchestype`
--

CREATE TABLE IF NOT EXISTS `mismatchestype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `AUTHORIZATIONSMISMATCH_MISMA_0` bigint(20) DEFAULT NULL,
  `OBLIGATIONSMISMATCH_MISMATCH_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK72B5F83697B40CA0` (`AUTHORIZATIONSMISMATCH_MISMA_0`),
  KEY `FK72B5F836362BE935` (`OBLIGATIONSMISMATCH_MISMATCH_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `mismatchestype`
--


-- --------------------------------------------------------

--
-- Table structure for table `mismatchpolicy`
--

CREATE TABLE IF NOT EXISTS `mismatchpolicy` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ELEMENTID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `mismatchpolicy`
--


-- --------------------------------------------------------

--
-- Table structure for table `missingattributedetailtype`
--

CREATE TABLE IF NOT EXISTS `missingattributedetailtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATTRIBUTEID` varchar(255) DEFAULT NULL,
  `DATATYPE` varchar(255) DEFAULT NULL,
  `ISSUER` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `missingattributedetailtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `nameidtype`
--

CREATE TABLE IF NOT EXISTS `nameidtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SPNAMEQUALIFIER` varchar(255) DEFAULT NULL,
  `SPPROVIDEDID` varchar(255) DEFAULT NULL,
  `FORMAT` varchar(255) DEFAULT NULL,
  `NAMEQUALIFIER` varchar(255) DEFAULT NULL,
  `VALUE_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `nameidtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `objecttype`
--

CREATE TABLE IF NOT EXISTS `objecttype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ENCODING_` varchar(255) DEFAULT NULL,
  `ID` varchar(255) DEFAULT NULL,
  `MIMETYPE` varchar(255) DEFAULT NULL,
  `OBJECT__SIGNATURETYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK5C8F66F9A2518107` (`OBJECT__SIGNATURETYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `objecttype`
--


-- --------------------------------------------------------

--
-- Table structure for table `objecttypecontentitem`
--

CREATE TABLE IF NOT EXISTS `objecttypecontentitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `CONTENTITEMS_OBJECTTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK30EA1253B2A322D8` (`CONTENTITEMS_OBJECTTYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `objecttypecontentitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `obligation`
--

CREATE TABLE IF NOT EXISTS `obligation` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACTIONNAME` varchar(255) DEFAULT NULL,
  `ELEMENTID` varchar(255) DEFAULT NULL,
  `MATCHING` bit(1) DEFAULT NULL,
  `MISMATCHID` varchar(255) DEFAULT NULL,
  `ACTIONVALUE_OBLIGATION_HJID` bigint(20) DEFAULT NULL,
  `TRIGGERSSET_OBLIGATION_HJID` bigint(20) DEFAULT NULL,
  `OBLIGATION_MISMATCHPOLICY_HJ_0` bigint(20) DEFAULT NULL,
  `OBLIGATION_TRIGGERONVIOLATIO_0` bigint(20) DEFAULT NULL,
  `OBLIGATION_OBLIGATIONSSET_0__0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK5B2157E1D063EB7` (`ACTIONVALUE_OBLIGATION_HJID`),
  KEY `FK5B2157EEE693C5` (`OBLIGATION_OBLIGATIONSSET_0__0`),
  KEY `FK5B2157EC12153EF` (`OBLIGATION_MISMATCHPOLICY_HJ_0`),
  KEY `FK5B2157E528D7946` (`TRIGGERSSET_OBLIGATION_HJID`),
  KEY `FK5B2157EFA943925` (`OBLIGATION_TRIGGERONVIOLATIO_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `obligation`
--


-- --------------------------------------------------------

--
-- Table structure for table `obligationsset`
--

CREATE TABLE IF NOT EXISTS `obligationsset` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MATCHING` bit(1) DEFAULT NULL,
  `MISMATCHID` varchar(255) DEFAULT NULL,
  `MISMATCHES_OBLIGATIONSSET_HJ_0` bigint(20) DEFAULT NULL,
  `OBLIGATIONSSET_OBLIGATIONSSE_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKBB6218DB021EADC` (`MISMATCHES_OBLIGATIONSSET_HJ_0`),
  KEY `FKBB6218D1CB9A57A` (`OBLIGATIONSSET_OBLIGATIONSSE_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `obligationsset`
--


-- --------------------------------------------------------

--
-- Table structure for table `obligationsset_0`
--

CREATE TABLE IF NOT EXISTS `obligationsset_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ELEMENTID` varchar(255) DEFAULT NULL,
  `INFINIT` bit(1) DEFAULT NULL,
  `MATCHING` bit(1) DEFAULT NULL,
  `MISMATCHID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `obligationsset_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `obligationstype`
--

CREATE TABLE IF NOT EXISTS `obligationstype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `obligationstype`
--


-- --------------------------------------------------------

--
-- Table structure for table `obligationtype`
--

CREATE TABLE IF NOT EXISTS `obligationtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FULFILLON` varchar(255) DEFAULT NULL,
  `OBLIGATIONID` varchar(255) DEFAULT NULL,
  `OBLIGATION_OBLIGATIONSTYPE_H_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKBB6ADB826766E07` (`OBLIGATION_OBLIGATIONSTYPE_H_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `obligationtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `onetimeusetype`
--

CREATE TABLE IF NOT EXISTS `onetimeusetype` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKB088EFAEF259A589` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `onetimeusetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `pgpdatatype`
--

CREATE TABLE IF NOT EXISTS `pgpdatatype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `pgpdatatype`
--


-- --------------------------------------------------------

--
-- Table structure for table `pgpdatatypecontentitem`
--

CREATE TABLE IF NOT EXISTS `pgpdatatypecontentitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `ITEMPGPKEYID` longblob,
  `ITEMPGPKEYPACKET` longblob,
  `CONTENTITEMS_PGPDATATYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK39F4E4AF76B13FC0` (`CONTENTITEMS_PGPDATATYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `pgpdatatypecontentitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `piitype`
--

CREATE TABLE IF NOT EXISTS `piitype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ATTRIBUTENAME` varchar(255) DEFAULT NULL,
  `ATTRIBUTEVALUE` varchar(255) DEFAULT NULL,
  `CREATIONDATEITEM` datetime DEFAULT NULL,
  `MODIFICATIONDATEITEM` datetime DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `piitype`
--


-- --------------------------------------------------------

--
-- Table structure for table `piitypepolicysetorpolicyitem`
--

CREATE TABLE IF NOT EXISTS `piitypepolicysetorpolicyitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMPOLICY_PIITYPEPOLICYSETO_0` bigint(20) DEFAULT NULL,
  `ITEMPOLICYSET_PIITYPEPOLICYS_0` bigint(20) DEFAULT NULL,
  `POLICYSETORPOLICYITEMS_PIITY_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK705AE0CEA2E7234F` (`POLICYSETORPOLICYITEMS_PIITY_0`),
  KEY `FK705AE0CE41AA662F` (`ITEMPOLICYSET_PIITYPEPOLICYS_0`),
  KEY `FK705AE0CEC166491` (`ITEMPOLICY_PIITYPEPOLICYSETO_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `piitypepolicysetorpolicyitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `piiuniqueid`
--

CREATE TABLE IF NOT EXISTS `piiuniqueid` (
  `id` bigint(20) NOT NULL,
  `uniqueId` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniqueId` (`uniqueId`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `piiuniqueid`
--


-- --------------------------------------------------------

--
-- Table structure for table `policycombinerparameterstype`
--

CREATE TABLE IF NOT EXISTS `policycombinerparameterstype` (
  `POLICYIDREF` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKB1B635A9FF14A864` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `policycombinerparameterstype`
--


-- --------------------------------------------------------

--
-- Table structure for table `policysetcombinerparameterst_0`
--

CREATE TABLE IF NOT EXISTS `policysetcombinerparameterst_0` (
  `POLICYSETIDREF` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK544994D8FF14A864` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `policysetcombinerparameterst_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `policysettype`
--

CREATE TABLE IF NOT EXISTS `policysettype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `POLICYCOMBININGALGID` varchar(255) DEFAULT NULL,
  `POLICYSETID` varchar(255) DEFAULT NULL,
  `VERSION_` varchar(255) DEFAULT NULL,
  `OBLIGATIONS_POLICYSETTYPE_HJ_0` bigint(20) DEFAULT NULL,
  `POLICYSETDEFAULTS_POLICYSETT_0` bigint(20) DEFAULT NULL,
  `TARGET_POLICYSETTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK51F6E10A2C9225FB` (`TARGET_POLICYSETTYPE_HJID`),
  KEY `FK51F6E10A31F0954E` (`POLICYSETDEFAULTS_POLICYSETT_0`),
  KEY `FK51F6E10ABD5AC8F1` (`OBLIGATIONS_POLICYSETTYPE_HJ_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `policysettype`
--


-- --------------------------------------------------------

--
-- Table structure for table `policysettypepolicysetorpoli_0`
--

CREATE TABLE IF NOT EXISTS `policysettypepolicysetorpoli_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMCOMBINERPARAMETERS_POLIC_1` bigint(20) DEFAULT NULL,
  `ITEMPOLICYCOMBINERPARAMETERS_0` bigint(20) DEFAULT NULL,
  `ITEMPOLICYIDREFERENCE_POLICY_0` bigint(20) DEFAULT NULL,
  `ITEMPOLICYSETCOMBINERPARAMET_1` bigint(20) DEFAULT NULL,
  `ITEMPOLICYSETIDREFERENCE_POL_0` bigint(20) DEFAULT NULL,
  `POLICYSETORPOLICYORPOLICYSET_1` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK4F647616793A4DCC` (`ITEMPOLICYCOMBINERPARAMETERS_0`),
  KEY `FK4F6476165E317CEC` (`ITEMPOLICYSETIDREFERENCE_POL_0`),
  KEY `FK4F647616216AEBF5` (`ITEMPOLICYSETCOMBINERPARAMET_1`),
  KEY `FK4F647616BE3391F1` (`ITEMPOLICYIDREFERENCE_POLICY_0`),
  KEY `FK4F6476161A3FC931` (`ITEMCOMBINERPARAMETERS_POLIC_1`),
  KEY `FK4F6476161041A7D4` (`POLICYSETORPOLICYORPOLICYSET_1`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `policysettypepolicysetorpoli_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `policysettype_0`
--

CREATE TABLE IF NOT EXISTS `policysettype_0` (
  `HJID` bigint(20) NOT NULL,
  `CREDENTIALREQUIREMENTS_POLIC_0` bigint(20) DEFAULT NULL,
  `DATAHANDLINGPREFERENCES_POLI_0` bigint(20) DEFAULT NULL,
  `PROVISIONALACTIONS_POLICYSET_0` bigint(20) DEFAULT NULL,
  `STICKYPOLICY_POLICYSETTYPE_0_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKAFC2D23B3F4F641B` (`PROVISIONALACTIONS_POLICYSET_0`),
  KEY `FKAFC2D23B75ABEEA1` (`DATAHANDLINGPREFERENCES_POLI_0`),
  KEY `FKAFC2D23B565FC01` (`CREDENTIALREQUIREMENTS_POLIC_0`),
  KEY `FKAFC2D23BE5A86837` (`HJID`),
  KEY `FKAFC2D23B50DA9F02` (`STICKYPOLICY_POLICYSETTYPE_0_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `policysettype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `policytype`
--

CREATE TABLE IF NOT EXISTS `policytype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `POLICYID` varchar(255) DEFAULT NULL,
  `RULECOMBININGALGID` varchar(255) DEFAULT NULL,
  `VERSION_` varchar(255) DEFAULT NULL,
  `OBLIGATIONS_POLICYTYPE_HJID` bigint(20) DEFAULT NULL,
  `POLICYDEFAULTS_POLICYTYPE_HJ_0` bigint(20) DEFAULT NULL,
  `TARGET_POLICYTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK7BF4376C76CF81A7` (`POLICYDEFAULTS_POLICYTYPE_HJ_0`),
  KEY `FK7BF4376CEA8FA0DD` (`TARGET_POLICYTYPE_HJID`),
  KEY `FK7BF4376CDF26A205` (`OBLIGATIONS_POLICYTYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `policytype`
--


-- --------------------------------------------------------

--
-- Table structure for table `policytypecombinerparameters_0`
--

CREATE TABLE IF NOT EXISTS `policytypecombinerparameters_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMCOMBINERPARAMETERS_POLIC_0` bigint(20) DEFAULT NULL,
  `ITEMRULECOMBINERPARAMETERS_P_0` bigint(20) DEFAULT NULL,
  `ITEMVARIABLEDEFINITION_POLIC_0` bigint(20) DEFAULT NULL,
  `COMBINERPARAMETERSORRULECOMB_1` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK5295C71A104B7251` (`ITEMRULECOMBINERPARAMETERS_P_0`),
  KEY `FK5295C71A65CBB86B` (`COMBINERPARAMETERSORRULECOMB_1`),
  KEY `FK5295C71A25685554` (`ITEMVARIABLEDEFINITION_POLIC_0`),
  KEY `FK5295C71A1A3FC930` (`ITEMCOMBINERPARAMETERS_POLIC_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `policytypecombinerparameters_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `policytype_0`
--

CREATE TABLE IF NOT EXISTS `policytype_0` (
  `HJID` bigint(20) NOT NULL,
  `CREDENTIALREQUIREMENTS_POLIC_1` bigint(20) DEFAULT NULL,
  `DATAHANDLINGPREFERENCES_POLI_1` bigint(20) DEFAULT NULL,
  `PROVISIONALACTIONS_POLICYTYP_0` bigint(20) DEFAULT NULL,
  `STICKYPOLICY_POLICYTYPE_0_HJ_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK4FC4181D75ABEEA2` (`DATAHANDLINGPREFERENCES_POLI_1`),
  KEY `FK4FC4181D3F668404` (`PROVISIONALACTIONS_POLICYTYP_0`),
  KEY `FK4FC4181DC75102CB` (`STICKYPOLICY_POLICYTYPE_0_HJ_0`),
  KEY `FK4FC4181D565FC02` (`CREDENTIALREQUIREMENTS_POLIC_1`),
  KEY `FK4FC4181D8C82E5B9` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `policytype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `pplpolicystatementtype`
--

CREATE TABLE IF NOT EXISTS `pplpolicystatementtype` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKCD7F974BF9AB83DD` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `pplpolicystatementtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `pplpolicystatementtypepolicy_0`
--

CREATE TABLE IF NOT EXISTS `pplpolicystatementtypepolicy_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMPOLICY_PPLPOLICYSTATEMEN_0` bigint(20) DEFAULT NULL,
  `ITEMPOLICYSET_PPLPOLICYSTATE_0` bigint(20) DEFAULT NULL,
  `POLICYORPOLICYSETITEMS_PPLPO_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKFB443CAE11ED818B` (`ITEMPOLICY_PPLPOLICYSTATEMEN_0`),
  KEY `FKFB443CAEDF1E766B` (`ITEMPOLICYSET_PPLPOLICYSTATE_0`),
  KEY `FKFB443CAE614D9277` (`POLICYORPOLICYSETITEMS_PPLPO_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `pplpolicystatementtypepolicy_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `primelifeapplytype`
--

CREATE TABLE IF NOT EXISTS `primelifeapplytype` (
  `DISCLOSE` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK37AF880DC7FFEAD5` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `primelifeapplytype`
--


-- --------------------------------------------------------

--
-- Table structure for table `provisionalactionstatementty_0`
--

CREATE TABLE IF NOT EXISTS `provisionalactionstatementty_0` (
  `EVIDENCEID` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKEE671CAFF9AB83DD` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `provisionalactionstatementty_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `provisionalactionstype`
--

CREATE TABLE IF NOT EXISTS `provisionalactionstype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `provisionalactionstype`
--


-- --------------------------------------------------------

--
-- Table structure for table `provisionalactiontype`
--

CREATE TABLE IF NOT EXISTS `provisionalactiontype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACTIONID` varchar(255) DEFAULT NULL,
  `PROVISIONALACTION_PROVISIONA_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK773D3E1047363502` (`PROVISIONALACTION_PROVISIONA_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `provisionalactiontype`
--


-- --------------------------------------------------------

--
-- Table structure for table `provisionalactiontype_0`
--

CREATE TABLE IF NOT EXISTS `provisionalactiontype_0` (
  `EVIDENCEID` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  `PROVISIONALACTION_PROVISIONA_1` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK9CE605C1850DACE4` (`PROVISIONALACTION_PROVISIONA_1`),
  KEY `FK9CE605C1BFDC0AE8` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `provisionalactiontype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `proxyrestrictiontype`
--

CREATE TABLE IF NOT EXISTS `proxyrestrictiontype` (
  `COUNT_` decimal(20,10) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK750100D8F259A589` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `proxyrestrictiontype`
--


-- --------------------------------------------------------

--
-- Table structure for table `proxyrestrictiontypeaudience_0`
--

CREATE TABLE IF NOT EXISTS `proxyrestrictiontypeaudience_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEM` varchar(255) DEFAULT NULL,
  `AUDIENCEITEMS_PROXYRESTRICTI_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKF1B6478D6DC70196` (`AUDIENCEITEMS_PROXYRESTRICTI_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `proxyrestrictiontypeaudience_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `purposelisttype`
--

CREATE TABLE IF NOT EXISTS `purposelisttype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `purposelisttype`
--


-- --------------------------------------------------------

--
-- Table structure for table `purposelisttypepurposeitem`
--

CREATE TABLE IF NOT EXISTS `purposelisttypepurposeitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEM` varchar(255) DEFAULT NULL,
  `PURPOSEITEMS_PURPOSELISTTYPE_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKC93B4F7B8EDDFCE7` (`PURPOSEITEMS_PURPOSELISTTYPE_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `purposelisttypepurposeitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `referencelist`
--

CREATE TABLE IF NOT EXISTS `referencelist` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `referencelist`
--


-- --------------------------------------------------------

--
-- Table structure for table `referencelistdatareferenceor_0`
--

CREATE TABLE IF NOT EXISTS `referencelistdatareferenceor_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMDATAREFERENCE_REFERENCEL_0` bigint(20) DEFAULT NULL,
  `ITEMKEYREFERENCE_REFERENCELI_0` bigint(20) DEFAULT NULL,
  `DATAREFERENCEORKEYREFERENCEI_1` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKEE5397EC65453073` (`ITEMDATAREFERENCE_REFERENCEL_0`),
  KEY `FKEE5397ECAAB9EE69` (`ITEMKEYREFERENCE_REFERENCELI_0`),
  KEY `FKEE5397ECA420837F` (`DATAREFERENCEORKEYREFERENCEI_1`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `referencelistdatareferenceor_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `referencetype`
--

CREATE TABLE IF NOT EXISTS `referencetype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `URI` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `referencetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `referencetypeanyitem`
--

CREATE TABLE IF NOT EXISTS `referencetypeanyitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMOBJECT` longtext,
  `ANYITEMS_REFERENCETYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKFE4A865A90F38DF2` (`ANYITEMS_REFERENCETYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `referencetypeanyitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `referencetype_0`
--

CREATE TABLE IF NOT EXISTS `referencetype_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `URI` varchar(255) DEFAULT NULL,
  `DIGESTVALUE` longblob,
  `ID` varchar(255) DEFAULT NULL,
  `TYPE_` varchar(255) DEFAULT NULL,
  `DIGESTMETHOD_REFERENCETYPE_0_0` bigint(20) DEFAULT NULL,
  `TRANSFORMS__REFERENCETYPE_0__0` bigint(20) DEFAULT NULL,
  `REFERENCE_SIGNEDINFOTYPE_HJID` bigint(20) DEFAULT NULL,
  `REFERENCE_MANIFESTTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKB00AAF567E8A7720` (`DIGESTMETHOD_REFERENCETYPE_0_0`),
  KEY `FKB00AAF569E41993C` (`REFERENCE_MANIFESTTYPE_HJID`),
  KEY `FKB00AAF567EF9D63C` (`REFERENCE_SIGNEDINFOTYPE_HJID`),
  KEY `FKB00AAF5614F12456` (`TRANSFORMS__REFERENCETYPE_0__0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `referencetype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `requesttype`
--

CREATE TABLE IF NOT EXISTS `requesttype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACTION__REQUESTTYPE_HJID` bigint(20) DEFAULT NULL,
  `ENVIRONMENT_REQUESTTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKC4EF362922ABD874` (`ACTION__REQUESTTYPE_HJID`),
  KEY `FKC4EF3629B6B7A3D1` (`ENVIRONMENT_REQUESTTYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `requesttype`
--


-- --------------------------------------------------------

--
-- Table structure for table `resourceattributedesignatort_0`
--

CREATE TABLE IF NOT EXISTS `resourceattributedesignatort_0` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK721E2B036A5CA237` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `resourceattributedesignatort_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `resourcecontenttype`
--

CREATE TABLE IF NOT EXISTS `resourcecontenttype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `resourcecontenttype`
--


-- --------------------------------------------------------

--
-- Table structure for table `resourcecontenttypecontentit_0`
--

CREATE TABLE IF NOT EXISTS `resourcecontenttypecontentit_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `CONTENTITEMS_RESOURCECONTENT_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKC77D6A908CB4278E` (`CONTENTITEMS_RESOURCECONTENT_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `resourcecontenttypecontentit_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `resourcematchtype`
--

CREATE TABLE IF NOT EXISTS `resourcematchtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MATCHID` varchar(255) DEFAULT NULL,
  `ATTRIBUTESELECTOR_RESOURCEMA_0` bigint(20) DEFAULT NULL,
  `ATTRIBUTEVALUE_RESOURCEMATCH_0` bigint(20) DEFAULT NULL,
  `RESOURCEATTRIBUTEDESIGNATOR__0` bigint(20) DEFAULT NULL,
  `RESOURCEMATCH_RESOURCETYPE_H_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKBB1D0A11E9EC3B62` (`RESOURCEMATCH_RESOURCETYPE_H_0`),
  KEY `FKBB1D0A1193E6C8DA` (`RESOURCEATTRIBUTEDESIGNATOR__0`),
  KEY `FKBB1D0A1174B87E7C` (`ATTRIBUTESELECTOR_RESOURCEMA_0`),
  KEY `FKBB1D0A116961BA1D` (`ATTRIBUTEVALUE_RESOURCEMATCH_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `resourcematchtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `resourcequerytype`
--

CREATE TABLE IF NOT EXISTS `resourcequerytype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DATAHANDLINGPOLICY_RESOURCEQ_0` bigint(20) DEFAULT NULL,
  `REQUEST_RESOURCEQUERYTYPE_HJ_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK2AD10F9442C28676` (`DATAHANDLINGPOLICY_RESOURCEQ_0`),
  KEY `FK2AD10F947F9DFDC` (`REQUEST_RESOURCEQUERYTYPE_HJ_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `resourcequerytype`
--


-- --------------------------------------------------------

--
-- Table structure for table `resourcestype`
--

CREATE TABLE IF NOT EXISTS `resourcestype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `resourcestype`
--


-- --------------------------------------------------------

--
-- Table structure for table `resourcetype`
--

CREATE TABLE IF NOT EXISTS `resourcetype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `RESOURCE__RESOURCESTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK734484484C81B85A` (`RESOURCE__RESOURCESTYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `resourcetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `resourcetype_0`
--

CREATE TABLE IF NOT EXISTS `resourcetype_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `RESOURCECONTENT_RESOURCETYPE_0` bigint(20) DEFAULT NULL,
  `RESOURCE__REQUESTTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKB4349DF94F29708F` (`RESOURCE__REQUESTTYPE_HJID`),
  KEY `FKB4349DF9A3197557` (`RESOURCECONTENT_RESOURCETYPE_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `resourcetype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `responsetype`
--

CREATE TABLE IF NOT EXISTS `responsetype` (
  `DECISION` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  `ACCESSPII_RESPONSETYPE_HJID` bigint(20) DEFAULT NULL,
  `DENEYPII_RESPONSETYPE_HJID` bigint(20) DEFAULT NULL,
  `MISSINGCREDENTIAL_RESPONSETY_0` bigint(20) DEFAULT NULL,
  `MISSINGPII_RESPONSETYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKDFF682FBCDEED7B0` (`ACCESSPII_RESPONSETYPE_HJID`),
  KEY `FKDFF682FBB0DB00EE` (`MISSINGPII_RESPONSETYPE_HJID`),
  KEY `FKDFF682FB14EB6FB3` (`DENEYPII_RESPONSETYPE_HJID`),
  KEY `FKDFF682FBEEB769B` (`MISSINGCREDENTIAL_RESPONSETY_0`),
  KEY `FKDFF682FBF9AB83DD` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `responsetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `responsetype_0`
--

CREATE TABLE IF NOT EXISTS `responsetype_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `responsetype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `resulttype`
--

CREATE TABLE IF NOT EXISTS `resulttype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DECISION` varchar(255) DEFAULT NULL,
  `RESOURCEID` varchar(255) DEFAULT NULL,
  `OBLIGATIONS_RESULTTYPE_HJID` bigint(20) DEFAULT NULL,
  `STATUS_RESULTTYPE_HJID` bigint(20) DEFAULT NULL,
  `RESULT__RESPONSETYPE_0_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKFAE2ECD7BA0C8F7F` (`STATUS_RESULTTYPE_HJID`),
  KEY `FKFAE2ECD718B7977A` (`OBLIGATIONS_RESULTTYPE_HJID`),
  KEY `FKFAE2ECD781CBF609` (`RESULT__RESPONSETYPE_0_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `resulttype`
--


-- --------------------------------------------------------

--
-- Table structure for table `retrievalmethodtype`
--

CREATE TABLE IF NOT EXISTS `retrievalmethodtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `URI` varchar(255) DEFAULT NULL,
  `TYPE_` varchar(255) DEFAULT NULL,
  `TRANSFORMS__RETRIEVALMETHODT_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK4E9901AF7A43C92C` (`TRANSFORMS__RETRIEVALMETHODT_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `retrievalmethodtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `rsakeyvaluetype`
--

CREATE TABLE IF NOT EXISTS `rsakeyvaluetype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EXPONENT` longblob,
  `MODULUS` longblob,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `rsakeyvaluetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `rulecombinerparameterstype`
--

CREATE TABLE IF NOT EXISTS `rulecombinerparameterstype` (
  `RULEIDREF` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKDE957F3FF14A864` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `rulecombinerparameterstype`
--


-- --------------------------------------------------------

--
-- Table structure for table `ruletype`
--

CREATE TABLE IF NOT EXISTS `ruletype` (
  `HJID` bigint(20) NOT NULL,
  `CREDENTIALREQUIREMENTS_RULET_0` bigint(20) DEFAULT NULL,
  `DATAHANDLINGPREFERENCES_RULE_0` bigint(20) DEFAULT NULL,
  `PROVISIONALACTIONS_RULETYPE__0` bigint(20) DEFAULT NULL,
  `STICKYPOLICY_RULETYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK5C36383666C16D4` (`PROVISIONALACTIONS_RULETYPE__0`),
  KEY `FK5C363836796DF492` (`CREDENTIALREQUIREMENTS_RULET_0`),
  KEY `FK5C363836796A1DE1` (`DATAHANDLINGPREFERENCES_RULE_0`),
  KEY `FK5C363836465B7DBC` (`STICKYPOLICY_RULETYPE_HJID`),
  KEY `FK5C36383671F0C683` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `ruletype`
--


-- --------------------------------------------------------

--
-- Table structure for table `ruletype_0`
--

CREATE TABLE IF NOT EXISTS `ruletype_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DESCRIPTION` varchar(255) DEFAULT NULL,
  `EFFECT` varchar(255) DEFAULT NULL,
  `RULEID` varchar(255) DEFAULT NULL,
  `CONDITION_RULETYPE_0_HJID` bigint(20) DEFAULT NULL,
  `TARGET_RULETYPE_0_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK27890E67617E72C2` (`TARGET_RULETYPE_0_HJID`),
  KEY `FK27890E67C871ADB6` (`CONDITION_RULETYPE_0_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `ruletype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `signaturemethodtype`
--

CREATE TABLE IF NOT EXISTS `signaturemethodtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ALGORITHM` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `signaturemethodtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `signaturemethodtypecontentit_0`
--

CREATE TABLE IF NOT EXISTS `signaturemethodtypecontentit_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMHMACOUTPUTLENGTH` decimal(19,2) DEFAULT NULL,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `CONTENTITEMS_SIGNATUREMETHOD_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK9EBDD5E2693638D1` (`CONTENTITEMS_SIGNATUREMETHOD_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `signaturemethodtypecontentit_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `signaturepropertiestype`
--

CREATE TABLE IF NOT EXISTS `signaturepropertiestype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `signaturepropertiestype`
--


-- --------------------------------------------------------

--
-- Table structure for table `signaturepropertytype`
--

CREATE TABLE IF NOT EXISTS `signaturepropertytype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID` varchar(255) DEFAULT NULL,
  `TARGET` varchar(255) DEFAULT NULL,
  `SIGNATUREPROPERTY_SIGNATUREP_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK5F4BE727C223214C` (`SIGNATUREPROPERTY_SIGNATUREP_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `signaturepropertytype`
--


-- --------------------------------------------------------

--
-- Table structure for table `signaturepropertytypecontent_0`
--

CREATE TABLE IF NOT EXISTS `signaturepropertytypecontent_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `CONTENTITEMS_SIGNATUREPROPER_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKAF2C4E83817D1574` (`CONTENTITEMS_SIGNATUREPROPER_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `signaturepropertytypecontent_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `signaturetype`
--

CREATE TABLE IF NOT EXISTS `signaturetype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID` varchar(255) DEFAULT NULL,
  `KEYINFO_SIGNATURETYPE_HJID` bigint(20) DEFAULT NULL,
  `SIGNATUREVALUE_SIGNATURETYPE_0` bigint(20) DEFAULT NULL,
  `SIGNEDINFO_SIGNATURETYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK1B8723D273E2E4CF` (`KEYINFO_SIGNATURETYPE_HJID`),
  KEY `FK1B8723D228DF4B04` (`SIGNATUREVALUE_SIGNATURETYPE_0`),
  KEY `FK1B8723D259B8BE97` (`SIGNEDINFO_SIGNATURETYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `signaturetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `signaturevaluetype`
--

CREATE TABLE IF NOT EXISTS `signaturevaluetype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID` varchar(255) DEFAULT NULL,
  `VALUE_` longblob,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `signaturevaluetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `signedinfotype`
--

CREATE TABLE IF NOT EXISTS `signedinfotype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ID` varchar(255) DEFAULT NULL,
  `CANONICALIZATIONMETHOD_SIGNE_0` bigint(20) DEFAULT NULL,
  `SIGNATUREMETHOD_SIGNEDINFOTY_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK31816404B64A464A` (`CANONICALIZATIONMETHOD_SIGNE_0`),
  KEY `FK318164044B8F2245` (`SIGNATUREMETHOD_SIGNEDINFOTY_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `signedinfotype`
--


-- --------------------------------------------------------

--
-- Table structure for table `spkidatatype`
--

CREATE TABLE IF NOT EXISTS `spkidatatype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `spkidatatype`
--


-- --------------------------------------------------------

--
-- Table structure for table `spkidatatypespkisexpandanyit_0`
--

CREATE TABLE IF NOT EXISTS `spkidatatypespkisexpandanyit_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `ITEMSPKISEXP` longblob,
  `SPKISEXPANDANYITEMS_SPKIDATA_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKC4C4B7580C5AC22` (`SPKISEXPANDANYITEMS_SPKIDATA_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `spkidatatypespkisexpandanyit_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `statementabstracttype`
--

CREATE TABLE IF NOT EXISTS `statementabstracttype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `statementabstracttype`
--


-- --------------------------------------------------------

--
-- Table structure for table `statuscodetype`
--

CREATE TABLE IF NOT EXISTS `statuscodetype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `VALUE_` varchar(255) DEFAULT NULL,
  `STATUSCODE_STATUSCODETYPE_HJ_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK7697209985264B8D` (`STATUSCODE_STATUSCODETYPE_HJ_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `statuscodetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `statusdetailtype`
--

CREATE TABLE IF NOT EXISTS `statusdetailtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `statusdetailtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `statusdetailtypeanyitem`
--

CREATE TABLE IF NOT EXISTS `statusdetailtypeanyitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `ANYITEMS_STATUSDETAILTYPE_HJ_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKDBDC93C2298EE02` (`ANYITEMS_STATUSDETAILTYPE_HJ_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `statusdetailtypeanyitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `statustype`
--

CREATE TABLE IF NOT EXISTS `statustype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `STATUSMESSAGE` varchar(255) DEFAULT NULL,
  `STATUSCODE_STATUSTYPE_HJID` bigint(20) DEFAULT NULL,
  `STATUSDETAIL_STATUSTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK2BC1022C7960C744` (`STATUSCODE_STATUSTYPE_HJID`),
  KEY `FK2BC1022C5DD3BE4C` (`STATUSDETAIL_STATUSTYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `statustype`
--


-- --------------------------------------------------------

--
-- Table structure for table `stickypolicy`
--

CREATE TABLE IF NOT EXISTS `stickypolicy` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MATCHING` bit(1) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `stickypolicy`
--


-- --------------------------------------------------------

--
-- Table structure for table `stickypolicystatementtype`
--

CREATE TABLE IF NOT EXISTS `stickypolicystatementtype` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK3140C08EF9AB83DD` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stickypolicystatementtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `stickypolicytype`
--

CREATE TABLE IF NOT EXISTS `stickypolicytype` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKF354CE15FEFFA9B0` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stickypolicytype`
--


-- --------------------------------------------------------

--
-- Table structure for table `subjectattributedesignatorty_0`
--

CREATE TABLE IF NOT EXISTS `subjectattributedesignatorty_0` (
  `SUBJECTCATEGORY` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK761D39FA6A5CA237` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `subjectattributedesignatorty_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `subjectconfirmationdatatype`
--

CREATE TABLE IF NOT EXISTS `subjectconfirmationdatatype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ADDRESS` varchar(255) DEFAULT NULL,
  `INRESPONSETO` varchar(255) DEFAULT NULL,
  `NOTBEFOREITEM` datetime DEFAULT NULL,
  `NOTONORAFTERITEM` datetime DEFAULT NULL,
  `RECIPIENT` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `subjectconfirmationdatatype`
--


-- --------------------------------------------------------

--
-- Table structure for table `subjectconfirmationdatatypec_0`
--

CREATE TABLE IF NOT EXISTS `subjectconfirmationdatatypec_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `TEXT` longtext,
  `CONTENTITEMS_SUBJECTCONFIRMA_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK8E17CEFC5C6F580` (`CONTENTITEMS_SUBJECTCONFIRMA_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `subjectconfirmationdatatypec_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `subjectconfirmationtype`
--

CREATE TABLE IF NOT EXISTS `subjectconfirmationtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `METHOD_` varchar(255) DEFAULT NULL,
  `BASEID_SUBJECTCONFIRMATIONTY_0` bigint(20) DEFAULT NULL,
  `ENCRYPTEDID_SUBJECTCONFIRMAT_0` bigint(20) DEFAULT NULL,
  `NAMEID_SUBJECTCONFIRMATIONTY_0` bigint(20) DEFAULT NULL,
  `SUBJECTCONFIRMATIONDATA_SUBJ_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKCC0021FBD032D835` (`ENCRYPTEDID_SUBJECTCONFIRMAT_0`),
  KEY `FKCC0021FB78A2D129` (`SUBJECTCONFIRMATIONDATA_SUBJ_0`),
  KEY `FKCC0021FBA66B8329` (`NAMEID_SUBJECTCONFIRMATIONTY_0`),
  KEY `FKCC0021FB24A9BFF7` (`BASEID_SUBJECTCONFIRMATIONTY_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `subjectconfirmationtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `subjectlocalitytype`
--

CREATE TABLE IF NOT EXISTS `subjectlocalitytype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `DNSNAME` varchar(255) DEFAULT NULL,
  `ADDRESS` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `subjectlocalitytype`
--


-- --------------------------------------------------------

--
-- Table structure for table `subjectmatchtype`
--

CREATE TABLE IF NOT EXISTS `subjectmatchtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `MATCHID` varchar(255) DEFAULT NULL,
  `ATTRIBUTESELECTOR_SUBJECTMAT_0` bigint(20) DEFAULT NULL,
  `ATTRIBUTEVALUE_SUBJECTMATCHT_0` bigint(20) DEFAULT NULL,
  `SUBJECTATTRIBUTEDESIGNATOR_S_0` bigint(20) DEFAULT NULL,
  `SUBJECTMATCH_SUBJECTTYPE_1_H_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK22C89AD35D19B342` (`SUBJECTMATCH_SUBJECTTYPE_1_H_0`),
  KEY `FK22C89AD319E3E21` (`ATTRIBUTEVALUE_SUBJECTMATCHT_0`),
  KEY `FK22C89AD3325C4C8E` (`ATTRIBUTESELECTOR_SUBJECTMAT_0`),
  KEY `FK22C89AD3A79AB897` (`SUBJECTATTRIBUTEDESIGNATOR_S_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `subjectmatchtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `subjectstype`
--

CREATE TABLE IF NOT EXISTS `subjectstype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `subjectstype`
--


-- --------------------------------------------------------

--
-- Table structure for table `subjecttype`
--

CREATE TABLE IF NOT EXISTS `subjecttype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `subjecttype`
--


-- --------------------------------------------------------

--
-- Table structure for table `subjecttypecontentitem`
--

CREATE TABLE IF NOT EXISTS `subjecttypecontentitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMBASEID_SUBJECTTYPECONTEN_0` bigint(20) DEFAULT NULL,
  `ITEMENCRYPTEDID_SUBJECTTYPEC_0` bigint(20) DEFAULT NULL,
  `ITEMNAMEID_SUBJECTTYPECONTEN_0` bigint(20) DEFAULT NULL,
  `ITEMSUBJECTCONFIRMATION_SUBJ_0` bigint(20) DEFAULT NULL,
  `CONTENTITEMS_SUBJECTTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK19DB54E69870E3A9` (`CONTENTITEMS_SUBJECTTYPE_HJID`),
  KEY `FK19DB54E6548AF4D7` (`ITEMNAMEID_SUBJECTTYPECONTEN_0`),
  KEY `FK19DB54E67D484A5` (`ITEMBASEID_SUBJECTTYPECONTEN_0`),
  KEY `FK19DB54E66AA6611C` (`ITEMSUBJECTCONFIRMATION_SUBJ_0`),
  KEY `FK19DB54E66DF55B7E` (`ITEMENCRYPTEDID_SUBJECTTYPEC_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `subjecttypecontentitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `subjecttype_0`
--

CREATE TABLE IF NOT EXISTS `subjecttype_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SUBJECTCATEGORY` varchar(255) DEFAULT NULL,
  `SUBJECT_REQUESTTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKF8F694775AF92D4` (`SUBJECT_REQUESTTYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `subjecttype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `subjecttype_1`
--

CREATE TABLE IF NOT EXISTS `subjecttype_1` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SUBJECT_SUBJECTSTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKF8F69478D6B6E379` (`SUBJECT_SUBJECTSTYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `subjecttype_1`
--


-- --------------------------------------------------------

--
-- Table structure for table `targettype`
--

CREATE TABLE IF NOT EXISTS `targettype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACTIONS_TARGETTYPE_HJID` bigint(20) DEFAULT NULL,
  `ENVIRONMENTS_TARGETTYPE_HJID` bigint(20) DEFAULT NULL,
  `RESOURCES_TARGETTYPE_HJID` bigint(20) DEFAULT NULL,
  `SUBJECTS_TARGETTYPE_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK39F9BC4BC4D272CA` (`SUBJECTS_TARGETTYPE_HJID`),
  KEY `FK39F9BC4B74CF1916` (`ACTIONS_TARGETTYPE_HJID`),
  KEY `FK39F9BC4B317C4366` (`RESOURCES_TARGETTYPE_HJID`),
  KEY `FK39F9BC4B9471743C` (`ENVIRONMENTS_TARGETTYPE_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `targettype`
--


-- --------------------------------------------------------

--
-- Table structure for table `transformstype`
--

CREATE TABLE IF NOT EXISTS `transformstype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `transformstype`
--


-- --------------------------------------------------------

--
-- Table structure for table `transformstype_0`
--

CREATE TABLE IF NOT EXISTS `transformstype_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `transformstype_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `transformtype`
--

CREATE TABLE IF NOT EXISTS `transformtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ALGORITHM` varchar(255) DEFAULT NULL,
  `TRANSFORM__TRANSFORMSTYPE_0__0` bigint(20) DEFAULT NULL,
  `TRANSFORM__TRANSFORMSTYPE_HJ_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK53777C86B26B6DFA` (`TRANSFORM__TRANSFORMSTYPE_HJ_0`),
  KEY `FK53777C86E8878DE8` (`TRANSFORM__TRANSFORMSTYPE_0__0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `transformtype`
--


-- --------------------------------------------------------

--
-- Table structure for table `transformtypecontentitem`
--

CREATE TABLE IF NOT EXISTS `transformtypecontentitem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `ITEMXPATH` varchar(255) DEFAULT NULL,
  `TEXT` longtext,
  `CONTENTITEMS_TRANSFORMTYPE_H_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK286AF2A65E3DD9FA` (`CONTENTITEMS_TRANSFORMTYPE_H_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `transformtypecontentitem`
--


-- --------------------------------------------------------

--
-- Table structure for table `triggerattime`
--

CREATE TABLE IF NOT EXISTS `triggerattime` (
  `HJID` bigint(20) NOT NULL,
  `MAXDELAY_TRIGGERATTIME_HJID` bigint(20) DEFAULT NULL,
  `START__TRIGGERATTIME_HJID` bigint(20) DEFAULT NULL,
  `TRIGGERATTIME_MISMATCHPOLICY_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK73975118EF0CB1BF` (`MAXDELAY_TRIGGERATTIME_HJID`),
  KEY `FK739751184831D3F6` (`TRIGGERATTIME_MISMATCHPOLICY_0`),
  KEY `FK73975118F83C9D95` (`START__TRIGGERATTIME_HJID`),
  KEY `FK739751182D3E2C2E` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `triggerattime`
--


-- --------------------------------------------------------

--
-- Table structure for table `triggerdatalost`
--

CREATE TABLE IF NOT EXISTS `triggerdatalost` (
  `HJID` bigint(20) NOT NULL,
  `MAXDELAY_TRIGGERDATALOST_HJID` bigint(20) DEFAULT NULL,
  `TRIGGERDATALOST_MISMATCHPOLI_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK35238B669485D431` (`MAXDELAY_TRIGGERDATALOST_HJID`),
  KEY `FK35238B668F94E932` (`TRIGGERDATALOST_MISMATCHPOLI_0`),
  KEY `FK35238B662D3E2C2E` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `triggerdatalost`
--


-- --------------------------------------------------------

--
-- Table structure for table `triggerdatasubjectaccess`
--

CREATE TABLE IF NOT EXISTS `triggerdatasubjectaccess` (
  `URL` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  `TRIGGERDATASUBJECTACCESS_MIS_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK52436F6EABC5B3D5` (`TRIGGERDATASUBJECTACCESS_MIS_0`),
  KEY `FK52436F6E2D3E2C2E` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `triggerdatasubjectaccess`
--


-- --------------------------------------------------------

--
-- Table structure for table `triggeronviolation`
--

CREATE TABLE IF NOT EXISTS `triggeronviolation` (
  `HJID` bigint(20) NOT NULL,
  `MAXDELAY_TRIGGERONVIOLATION__0` bigint(20) DEFAULT NULL,
  `TRIGGERONVIOLATION_MISMATCHP_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKA113416E5FC535E0` (`TRIGGERONVIOLATION_MISMATCHP_0`),
  KEY `FKA113416E270EA25D` (`MAXDELAY_TRIGGERONVIOLATION__0`),
  KEY `FKA113416E2D3E2C2E` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `triggeronviolation`
--


-- --------------------------------------------------------

--
-- Table structure for table `triggerperiodic`
--

CREATE TABLE IF NOT EXISTS `triggerperiodic` (
  `HJID` bigint(20) NOT NULL,
  `END__TRIGGERPERIODIC_HJID` bigint(20) DEFAULT NULL,
  `MAXDELAY_TRIGGERPERIODIC_HJID` bigint(20) DEFAULT NULL,
  `PERIOD_TRIGGERPERIODIC_HJID` bigint(20) DEFAULT NULL,
  `START__TRIGGERPERIODIC_HJID` bigint(20) DEFAULT NULL,
  `TRIGGERPERIODIC_MISMATCHPOLI_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKE452D033DBDA92DA` (`START__TRIGGERPERIODIC_HJID`),
  KEY `FKE452D03372128784` (`MAXDELAY_TRIGGERPERIODIC_HJID`),
  KEY `FKE452D0339B588DA2` (`PERIOD_TRIGGERPERIODIC_HJID`),
  KEY `FKE452D033C397C693` (`END__TRIGGERPERIODIC_HJID`),
  KEY `FKE452D0331D43D0C5` (`TRIGGERPERIODIC_MISMATCHPOLI_0`),
  KEY `FKE452D0332D3E2C2E` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `triggerperiodic`
--


-- --------------------------------------------------------

--
-- Table structure for table `triggerpersonaldataaccessedf_0`
--

CREATE TABLE IF NOT EXISTS `triggerpersonaldataaccessedf_0` (
  `HJID` bigint(20) NOT NULL,
  `MAXDELAY_TRIGGERPERSONALDATA_0` bigint(20) DEFAULT NULL,
  `TRIGGERPERSONALDATAACCESSEDF_2` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKF662E5D2D085D90E` (`MAXDELAY_TRIGGERPERSONALDATA_0`),
  KEY `FKF662E5D24FA62172` (`TRIGGERPERSONALDATAACCESSEDF_2`),
  KEY `FKF662E5D22D3E2C2E` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `triggerpersonaldataaccessedf_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `triggerpersonaldataaccessedf_1`
--

CREATE TABLE IF NOT EXISTS `triggerpersonaldataaccessedf_1` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEM` varchar(255) DEFAULT NULL,
  `PURPOSEITEMS_TRIGGERPERSONAL_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKF662E5D3E1E8601F` (`PURPOSEITEMS_TRIGGERPERSONAL_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `triggerpersonaldataaccessedf_1`
--


-- --------------------------------------------------------

--
-- Table structure for table `triggerpersonaldatadeleted`
--

CREATE TABLE IF NOT EXISTS `triggerpersonaldatadeleted` (
  `HJID` bigint(20) NOT NULL,
  `MAXDELAY_TRIGGERPERSONALDATA_1` bigint(20) DEFAULT NULL,
  `TRIGGERPERSONALDATADELETED_M_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK7EE0A37D085D90F` (`MAXDELAY_TRIGGERPERSONALDATA_1`),
  KEY `FK7EE0A374A412274` (`TRIGGERPERSONALDATADELETED_M_0`),
  KEY `FK7EE0A372D3E2C2E` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `triggerpersonaldatadeleted`
--


-- --------------------------------------------------------

--
-- Table structure for table `triggerpersonaldatasent`
--

CREATE TABLE IF NOT EXISTS `triggerpersonaldatasent` (
  `ID` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  `MAXDELAY_TRIGGERPERSONALDATA_2` bigint(20) DEFAULT NULL,
  `TRIGGERPERSONALDATASENT_MISM_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKE5A5521AF975906A` (`TRIGGERPERSONALDATASENT_MISM_0`),
  KEY `FKE5A5521AD085D910` (`MAXDELAY_TRIGGERPERSONALDATA_2`),
  KEY `FKE5A5521A2D3E2C2E` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `triggerpersonaldatasent`
--


-- --------------------------------------------------------

--
-- Table structure for table `triggersset`
--

CREATE TABLE IF NOT EXISTS `triggersset` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ELEMENTID` varchar(255) DEFAULT NULL,
  `MATCHING` bit(1) DEFAULT NULL,
  `MISMATCHID` varchar(255) DEFAULT NULL,
  `NAME_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `triggersset`
--


-- --------------------------------------------------------

--
-- Table structure for table `triggerssettriggeritem`
--

CREATE TABLE IF NOT EXISTS `triggerssettriggeritem` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMNAME` varchar(255) DEFAULT NULL,
  `ITEMVALUE_TRIGGERSSETTRIGGER_0` bigint(20) DEFAULT NULL,
  `TRIGGERITEMS_TRIGGERSSET_HJID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK4BAC59A4CB495474` (`ITEMVALUE_TRIGGERSSETTRIGGER_0`),
  KEY `FK4BAC59A44BC2956C` (`TRIGGERITEMS_TRIGGERSSET_HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `triggerssettriggeritem`
--


-- --------------------------------------------------------

--
-- Table structure for table `trigger_`
--

CREATE TABLE IF NOT EXISTS `trigger_` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ELEMENTID` varchar(255) DEFAULT NULL,
  `MATCHING` bit(1) DEFAULT NULL,
  `MISMATCHID` varchar(255) DEFAULT NULL,
  `NAME_` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `trigger_`
--


-- --------------------------------------------------------

--
-- Table structure for table `undisclosedexpressiontype`
--

CREATE TABLE IF NOT EXISTS `undisclosedexpressiontype` (
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK2B8680F3A1AE81F` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `undisclosedexpressiontype`
--


-- --------------------------------------------------------

--
-- Table structure for table `undisclosedexpressiontypeatt_0`
--

CREATE TABLE IF NOT EXISTS `undisclosedexpressiontypeatt_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEM` varchar(255) DEFAULT NULL,
  `ATTRIBUTEIDITEMS_UNDISCLOSED_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK26EE8CDF8E2C500F` (`ATTRIBUTEIDITEMS_UNDISCLOSED_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `undisclosedexpressiontypeatt_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `variabledefinitiontype`
--

CREATE TABLE IF NOT EXISTS `variabledefinitiontype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EXPRESSIONNAME` varchar(255) DEFAULT NULL,
  `VARIABLEID` varchar(255) DEFAULT NULL,
  `EXPRESSIONVALUE_VARIABLEDEFI_0` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK642A80898D90E6D9` (`EXPRESSIONVALUE_VARIABLEDEFI_0`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `variabledefinitiontype`
--


-- --------------------------------------------------------

--
-- Table structure for table `variablereferencetype`
--

CREATE TABLE IF NOT EXISTS `variablereferencetype` (
  `VARIABLEID` varchar(255) DEFAULT NULL,
  `HJID` bigint(20) NOT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FK43876FE9A1AE81F` (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `variablereferencetype`
--


-- --------------------------------------------------------

--
-- Table structure for table `x509datatype`
--

CREATE TABLE IF NOT EXISTS `x509datatype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `x509datatype`
--


-- --------------------------------------------------------

--
-- Table structure for table `x509datatypex509issuerserial_0`
--

CREATE TABLE IF NOT EXISTS `x509datatypex509issuerserial_0` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ITEMELEMENT` longtext,
  `ITEMOBJECT` longtext,
  `ITEMX509CRL` longblob,
  `ITEMX509CERTIFICATE` longblob,
  `ITEMX509SKI` longblob,
  `ITEMX509SUBJECTNAME` longtext,
  `ITEMX509ISSUERSERIAL_X509DAT_0` bigint(20) DEFAULT NULL,
  `X509ISSUERSERIALORX509SKIORX_1` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`HJID`),
  KEY `FKB267A48E758927CA` (`ITEMX509ISSUERSERIAL_X509DAT_0`),
  KEY `FKB267A48E3707B3B0` (`X509ISSUERSERIALORX509SKIORX_1`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `x509datatypex509issuerserial_0`
--


-- --------------------------------------------------------

--
-- Table structure for table `x509issuerserialtype`
--

CREATE TABLE IF NOT EXISTS `x509issuerserialtype` (
  `HJID` bigint(20) NOT NULL AUTO_INCREMENT,
  `X509ISSUERNAME` varchar(255) DEFAULT NULL,
  `X509SERIALNUMBER` decimal(20,10) DEFAULT NULL,
  PRIMARY KEY (`HJID`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- Dumping data for table `x509issuerserialtype`
--

