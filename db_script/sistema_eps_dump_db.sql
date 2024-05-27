CREATE DATABASE  IF NOT EXISTS `datos_eps` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `datos_eps`;
-- MySQL dump 10.13  Distrib 8.0.36, for Linux (x86_64)
--
-- Host: localhost    Database: datos_eps
-- ------------------------------------------------------
-- Server version	8.0.36-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `acta`
--

DROP TABLE IF EXISTS `acta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `acta` (
  `id_acta` int NOT NULL AUTO_INCREMENT,
  `correlativo` varchar(100) NOT NULL,
  `fecha` date NOT NULL,
  `fecha_evaluacion` date DEFAULT NULL,
  `hora_inicio_evaluacion` datetime DEFAULT NULL,
  `hora_fin_evaluacion` datetime NOT NULL,
  `nota` int NOT NULL,
  `semestre` enum('PRIMER SEMESTRE','SEGUNDO SEMESTRE') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `salon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `resultado` enum('APROBADO','APROBADO CON CORRECCIONES','RECHAZADO') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `activo` tinyint(1) NOT NULL,
  `acta_generada` tinyint(1) NOT NULL,
  `comentario` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_proyecto_fk` int NOT NULL,
  `tipo` enum('ANTEPROYECTO','EXAMEN GENERAL','FINALIZACION') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id_acta`),
  KEY `acta_proyecto_FK` (`id_proyecto_fk`),
  CONSTRAINT `acta_proyecto_FK` FOREIGN KEY (`id_proyecto_fk`) REFERENCES `proyecto` (`id_proyecto`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `acta`
--

LOCK TABLES `acta` WRITE;
/*!40000 ALTER TABLE `acta` DISABLE KEYS */;
/*!40000 ALTER TABLE `acta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `bitacora`
--

DROP TABLE IF EXISTS `bitacora`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bitacora` (
  `id_bitacora` int NOT NULL AUTO_INCREMENT,
  `descripcion` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `avance` int NOT NULL,
  `numero_folio` int NOT NULL,
  `revision_asesor` tinyint(1) NOT NULL DEFAULT '0',
  `revision_supervisor` tinyint(1) NOT NULL DEFAULT '0',
  `revision_contraparte` tinyint(1) NOT NULL DEFAULT '0',
  `contiene_informe` tinyint(1) NOT NULL,
  `fecha_reporte_inicio` date NOT NULL,
  `fecha_reporte_fin` date NOT NULL,
  `fecha` date NOT NULL,
  `id_proyecto_fk` int NOT NULL,
  PRIMARY KEY (`id_bitacora`),
  KEY `bitacora_FK` (`id_proyecto_fk`),
  CONSTRAINT `bitacora_FK` FOREIGN KEY (`id_proyecto_fk`) REFERENCES `proyecto` (`id_proyecto`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bitacora`
--

LOCK TABLES `bitacora` WRITE;
/*!40000 ALTER TABLE `bitacora` DISABLE KEYS */;
/*!40000 ALTER TABLE `bitacora` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cambio_usuario`
--

DROP TABLE IF EXISTS `cambio_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `cambio_usuario` (
  `id_cambio_usuario` int NOT NULL AUTO_INCREMENT,
  `fecha` date NOT NULL,
  `motivo` varchar(400) DEFAULT NULL,
  `id_rol_fk` int NOT NULL,
  `id_usuario_proyecto_anterior_fk` int NOT NULL,
  `id_usuario_proyecto_nuevo_fk` int NOT NULL,
  PRIMARY KEY (`id_cambio_usuario`),
  KEY `cambio_usuario_FK` (`id_usuario_proyecto_anterior_fk`),
  KEY `cambio_usuario_FK_1` (`id_usuario_proyecto_nuevo_fk`),
  KEY `cambio_usuario_FK_2` (`id_rol_fk`),
  CONSTRAINT `cambio_usuario_FK` FOREIGN KEY (`id_usuario_proyecto_anterior_fk`) REFERENCES `usuario_proyecto` (`id_usuario_proyecto`),
  CONSTRAINT `cambio_usuario_FK_1` FOREIGN KEY (`id_usuario_proyecto_nuevo_fk`) REFERENCES `usuario_proyecto` (`id_usuario_proyecto`),
  CONSTRAINT `cambio_usuario_FK_2` FOREIGN KEY (`id_rol_fk`) REFERENCES `rol` (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cambio_usuario`
--

LOCK TABLES `cambio_usuario` WRITE;
/*!40000 ALTER TABLE `cambio_usuario` DISABLE KEYS */;
/*!40000 ALTER TABLE `cambio_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carrera`
--

DROP TABLE IF EXISTS `carrera`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrera` (
  `id_carrera` int NOT NULL,
  `nombre` varchar(150) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nombre_corto` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  PRIMARY KEY (`id_carrera`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrera`
--

LOCK TABLES `carrera` WRITE;
/*!40000 ALTER TABLE `carrera` DISABLE KEYS */;
INSERT INTO `carrera` VALUES (1,'Ingenieria en Ciencias y Sistemas','Ciencias y Sistemas'),(2,'Ingenieria Civil','Civil'),(3,'Ingenieria Mecánica','Mecánica'),(4,'Ingenieria Industrial','Industrial'),(5,'Ingenieria Mecánica Industrial','Mecánica Industrial');
/*!40000 ALTER TABLE `carrera` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carrera_usuario`
--

DROP TABLE IF EXISTS `carrera_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `carrera_usuario` (
  `id_carrera_usuario` int NOT NULL AUTO_INCREMENT,
  `id_usuario_fk` int NOT NULL,
  `id_carrera_fk` int NOT NULL,
  `cantidad_proyectos` int DEFAULT '0',
  PRIMARY KEY (`id_carrera_usuario`),
  KEY `fk_USUARIOS_has_CARRERA_CARRERA1_idx` (`id_carrera_fk`),
  KEY `fk_USUARIOS_has_CARRERA_USUARIOS1_idx` (`id_usuario_fk`),
  CONSTRAINT `fk_USUARIOS_has_CARRERA_CARRERA1` FOREIGN KEY (`id_carrera_fk`) REFERENCES `carrera` (`id_carrera`),
  CONSTRAINT `fk_USUARIOS_has_CARRERA_USUARIOS1` FOREIGN KEY (`id_usuario_fk`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=161 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carrera_usuario`
--

LOCK TABLES `carrera_usuario` WRITE;
/*!40000 ALTER TABLE `carrera_usuario` DISABLE KEYS */;
INSERT INTO `carrera_usuario` VALUES (1,1,1,NULL),(2,2,1,0);
/*!40000 ALTER TABLE `carrera_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comentario`
--

DROP TABLE IF EXISTS `comentario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comentario` (
  `id_comentario` int NOT NULL AUTO_INCREMENT,
  `comentario` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fecha_creacion` date NOT NULL,
  `id_usuario_fk` int NOT NULL,
  `id_rol_fk` int NOT NULL,
  `etapa_proyecto_fk` int DEFAULT NULL,
  PRIMARY KEY (`id_comentario`),
  KEY `fk_ETAPAS_PROYECTO_has_USUARIOS_USUARIOS1_idx` (`id_usuario_fk`),
  KEY `fk_ETAPAS_PROYECTO_has_USUARIOS_ETAPAS_PROYECTO1_idx` (`etapa_proyecto_fk`),
  KEY `comentario_FK` (`id_rol_fk`),
  CONSTRAINT `comentario_FK` FOREIGN KEY (`id_rol_fk`) REFERENCES `rol` (`id_rol`),
  CONSTRAINT `fk_ETAPAS_PROYECTO_has_USUARIOS_ETAPAS_PROYECTO1` FOREIGN KEY (`etapa_proyecto_fk`) REFERENCES `etapa_proyecto` (`id_etapa_proyecto`),
  CONSTRAINT `fk_ETAPAS_PROYECTO_has_USUARIOS_USUARIOS1` FOREIGN KEY (`id_usuario_fk`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=171 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comentario`
--

LOCK TABLES `comentario` WRITE;
/*!40000 ALTER TABLE `comentario` DISABLE KEYS */;
/*!40000 ALTER TABLE `comentario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comentario_bitacora`
--

DROP TABLE IF EXISTS `comentario_bitacora`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comentario_bitacora` (
  `id_comentario_bitacora` int NOT NULL AUTO_INCREMENT,
  `comentario` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fecha` date NOT NULL,
  `id_rol_fk` int NOT NULL,
  `id_usuario_fk` int NOT NULL,
  `id_bitacora_fk` int NOT NULL,
  PRIMARY KEY (`id_comentario_bitacora`),
  KEY `comentario_bitacora_FK` (`id_rol_fk`),
  KEY `comentario_bitacora_FK_1` (`id_usuario_fk`),
  KEY `comentario_bitacora_FK_2` (`id_bitacora_fk`),
  CONSTRAINT `comentario_bitacora_FK` FOREIGN KEY (`id_rol_fk`) REFERENCES `rol` (`id_rol`),
  CONSTRAINT `comentario_bitacora_FK_1` FOREIGN KEY (`id_usuario_fk`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `comentario_bitacora_FK_2` FOREIGN KEY (`id_bitacora_fk`) REFERENCES `bitacora` (`id_bitacora`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comentario_bitacora`
--

LOCK TABLES `comentario_bitacora` WRITE;
/*!40000 ALTER TABLE `comentario_bitacora` DISABLE KEYS */;
/*!40000 ALTER TABLE `comentario_bitacora` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `constante`
--

DROP TABLE IF EXISTS `constante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `constante` (
  `id_constante` int NOT NULL,
  `nombre` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `valor` varchar(45) NOT NULL,
  PRIMARY KEY (`id_constante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `constante`
--

LOCK TABLES `constante` WRITE;
/*!40000 ALTER TABLE `constante` DISABLE KEYS */;
/*!40000 ALTER TABLE `constante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `convocatoria`
--

DROP TABLE IF EXISTS `convocatoria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `convocatoria` (
  `id_convocatoria` int NOT NULL AUTO_INCREMENT,
  `correlativo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fecha` date NOT NULL,
  `fecha_evaluacion` date NOT NULL,
  `hora_evaluacion` datetime NOT NULL,
  `salon` varchar(200) NOT NULL,
  `activo` tinyint(1) NOT NULL,
  `comentario` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `id_titulo_representante_fk` int DEFAULT NULL,
  `nombre_representante` varchar(200) DEFAULT NULL,
  `id_proyecto_fk` int NOT NULL,
  `tipo` enum('ANTEPROYECTO','EXAMEN GENERAL') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_supervisor_fk` int DEFAULT NULL,
  `id_coordinador_carrera_fk` int DEFAULT NULL,
  `id_asesor_fk` int DEFAULT NULL,
  `id_coordinador_eps_fk` int DEFAULT NULL,
  PRIMARY KEY (`id_convocatoria`),
  KEY `convocatoria_titulo_FK` (`id_titulo_representante_fk`),
  KEY `convocatoria_proyecto_FK` (`id_proyecto_fk`),
  KEY `convocatoria_usuario_FK` (`id_supervisor_fk`),
  KEY `convocatoria_usuario_FK_1` (`id_coordinador_carrera_fk`),
  KEY `convocatoria_usuario_FK_2` (`id_asesor_fk`),
  KEY `convocatoria_usuario_FK_3` (`id_coordinador_eps_fk`),
  CONSTRAINT `convocatoria_proyecto_FK` FOREIGN KEY (`id_proyecto_fk`) REFERENCES `proyecto` (`id_proyecto`),
  CONSTRAINT `convocatoria_titulo_FK` FOREIGN KEY (`id_titulo_representante_fk`) REFERENCES `titulo` (`id_titulo`),
  CONSTRAINT `convocatoria_usuario_FK` FOREIGN KEY (`id_supervisor_fk`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `convocatoria_usuario_FK_1` FOREIGN KEY (`id_coordinador_carrera_fk`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `convocatoria_usuario_FK_2` FOREIGN KEY (`id_asesor_fk`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `convocatoria_usuario_FK_3` FOREIGN KEY (`id_coordinador_eps_fk`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `convocatoria`
--

LOCK TABLES `convocatoria` WRITE;
/*!40000 ALTER TABLE `convocatoria` DISABLE KEYS */;
/*!40000 ALTER TABLE `convocatoria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `correlativo`
--

DROP TABLE IF EXISTS `correlativo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `correlativo` (
  `id_correlativo` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `numeracion_actual` int NOT NULL,
  `ultima_actualizacion` date DEFAULT NULL,
  `id_carrera_fk` int DEFAULT NULL,
  `id_etapa_fk` int NOT NULL,
  PRIMARY KEY (`id_correlativo`),
  KEY `correlativo_FK` (`id_carrera_fk`),
  KEY `correlativo_FK_1` (`id_etapa_fk`),
  CONSTRAINT `correlativo_FK` FOREIGN KEY (`id_carrera_fk`) REFERENCES `carrera` (`id_carrera`),
  CONSTRAINT `correlativo_FK_1` FOREIGN KEY (`id_etapa_fk`) REFERENCES `etapa` (`id_etapa`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `correlativo`
--

LOCK TABLES `correlativo` WRITE;
/*!40000 ALTER TABLE `correlativo` DISABLE KEYS */;
INSERT INTO `correlativo` VALUES (1,'CONVOCATORIA ANTEPROYECTO',0,'2024-01-06',1,4),(2,'CONVOCATORIA ANTEPROYECTO',0,'2024-01-06',2,4),(3,'CONVOCATORIA ANTEPROYECTO',0,'2024-01-06',3,4),(4,'CONVOCATORIA ANTEPROYECTO',0,'2024-01-06',4,4),(5,'CONVOCATORIA ANTEPROYECTO',0,'2024-01-06',5,4),(6,'ACTA ANTEPROYECTO',0,'2024-01-06',NULL,6),(7,'CONVOCATORIA EXAMEN GENERAL',0,'2024-01-06',NULL,12),(8,'ACTA EXAMEN GENERAL',0,'2024-01-06',NULL,14),(9,'ACTA FINALIZACION',0,'2024-01-06',NULL,18);
/*!40000 ALTER TABLE `correlativo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `correlativo_estudiante`
--

DROP TABLE IF EXISTS `correlativo_estudiante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `correlativo_estudiante` (
  `id_correlativo_estudiante` int NOT NULL AUTO_INCREMENT,
  `numero_correlativo` int NOT NULL,
  `correlativo` varchar(100) NOT NULL,
  `fecha` date NOT NULL,
  `fecha_hora_evaluacion_anteproyecto` datetime DEFAULT NULL,
  `resultado_evaluacion_anteproyecto` enum('APROBADO','APROBADO CON CAMBIOS','REPROBADO') DEFAULT NULL,
  `anulado` tinyint(1) NOT NULL DEFAULT '0',
  `id_correlativo_fk` int NOT NULL,
  `id_estudiante_fk` int NOT NULL,
  PRIMARY KEY (`id_correlativo_estudiante`),
  KEY `correlativo_estudiante_FK` (`id_estudiante_fk`),
  KEY `correlativo_estudiante_FK_1` (`id_correlativo_fk`),
  CONSTRAINT `correlativo_estudiante_FK` FOREIGN KEY (`id_estudiante_fk`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `correlativo_estudiante_FK_1` FOREIGN KEY (`id_correlativo_fk`) REFERENCES `correlativo` (`id_correlativo`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `correlativo_estudiante`
--

LOCK TABLES `correlativo_estudiante` WRITE;
/*!40000 ALTER TABLE `correlativo_estudiante` DISABLE KEYS */;
/*!40000 ALTER TABLE `correlativo_estudiante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departamento`
--

DROP TABLE IF EXISTS `departamento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departamento` (
  `id_departamento` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`id_departamento`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departamento`
--

LOCK TABLES `departamento` WRITE;
/*!40000 ALTER TABLE `departamento` DISABLE KEYS */;
INSERT INTO `departamento` VALUES (1,'Alta Verapaz'),(2,'Baja Verapaz'),(3,'Chimaltenango'),(4,'Chiquimula'),(5,'El Progreso'),(6,'Escuintla'),(7,'Guatemala'),(8,'Huehuetenango'),(9,'Izabal'),(10,'Jalapa'),(11,'Jutiapa'),(12,'Petén'),(13,'Quetzaltenango'),(14,'Quiché'),(15,'Retalhuleu'),(16,'Sacatepéquez'),(17,'San Marcos'),(18,'Santa Rosa'),(19,'Solola'),(20,'Suchitepéquez'),(21,'Totonicapán'),(22,'Zacapa');
/*!40000 ALTER TABLE `departamento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `documento`
--

DROP TABLE IF EXISTS `documento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `documento` (
  `id_documento` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) NOT NULL,
  `link` varchar(200) NOT NULL,
  `categoria` enum('Evaluacion') NOT NULL,
  PRIMARY KEY (`id_documento`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `documento`
--

LOCK TABLES `documento` WRITE;
/*!40000 ALTER TABLE `documento` DISABLE KEYS */;
INSERT INTO `documento` VALUES (1,'Rubrica Evaluacion','documentos/RubricaEvaluacion.pdf','Evaluacion');
/*!40000 ALTER TABLE `documento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `elemento`
--

DROP TABLE IF EXISTS `elemento`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `elemento` (
  `id_elemento` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nombre_archivo` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `tipo` varchar(15) NOT NULL,
  `template` varchar(500) DEFAULT NULL,
  `id_etapa_fk` int NOT NULL,
  PRIMARY KEY (`id_elemento`),
  KEY `fk_ELEMENTO_ETAPA1_idx` (`id_etapa_fk`),
  CONSTRAINT `fk_ELEMENTO_ETAPA1` FOREIGN KEY (`id_etapa_fk`) REFERENCES `etapa` (`id_etapa`)
) ENGINE=InnoDB AUTO_INCREMENT=29 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `elemento`
--

LOCK TABLES `elemento` WRITE;
/*!40000 ALTER TABLE `elemento` DISABLE KEYS */;
INSERT INTO `elemento` VALUES (1,'Titulo','titulo','TEXT',NULL,1),(2,'Anteproyecto','anteproyecto.pdf','PDF',NULL,1),(3,'Constancia Inscripcion','constancia-inscripcion.pdf','PDF',NULL,1),(4,'Constancia Propedeutico','constancia-propedeutico.pdf','PDF',NULL,1),(5,'Certificado Nacimiento','certificado-nacimiento.pdf','PDF',NULL,1),(6,'Carta Asesor','carta-asesor.pdf','PDF',NULL,1),(7,'Finiquito AEIO','finiquito-aeio.pdf','PDF',NULL,1),(8,'Convocatoria Oficio','convocatoria-anteproyecto.pdf','PDF','templates/convocatoria_anteproyecto.docx',4),(9,'Convocatoria Oficio Firmada','convocatoria-anteproyecto-firmada.pdf','PDF',NULL,5),(10,'Acta Resultado Anteproyecto','acta-anteproyecto.pdf','PDF','templates/acta_anteproyecto.docx',6),(11,'Carta Aceptacion Contraparte','carta-aceptacion-contraparte.pdf','PDF','',7),(12,'Carta Finalizacion Asesor','Carta Finalizacion Asesor','PDF',NULL,11),(13,'Finiquito Contraparte','finiquito-contraparte.pdf','PDF',NULL,9),(14,'Informe Final ','infome-final.pdf','PDF',NULL,11),(15,'Convocatoria Examen General','convocatoria-examen-general.pdf','PDF','templates/convocatoria_examen_general.docx',12),(16,'Convocatoria Examen General Firmada','convocatoria-examen-general-firmada.pdf','PDF',NULL,13),(17,'Acta Examen General','acta-examen-general.pdf','PDF','templates/acta_examen_general.docx',14),(18,'Articulo Cientifico','articulo-cientifico.pdf','PDF',NULL,15),(19,'Carta Traductor Jurado','carta-traductor-jurado.pdf','PDF',NULL,15),(20,'Constancia Revision Linguistica','constancia-revision-linguistica.pdf','PDF',NULL,15),(21,'Dictamen Supervisor','dictamen-supervisor.pdf','PDF',NULL,17),(22,'Carta Revisor','carta-revisor.pdf','PDF',NULL,17),(23,'Acta Finalizacion','acta-finalizacion.pdf','PDF','templates/acta_finalizacion.docx',18),(24,'Constancia Cierre','constancia-cierre.pdf','PDF',NULL,1),(25,'Solicitud Esutdiante','solicitud.pdf','PDF',NULL,1),(26,'Otros','otros.pdf','PDF',NULL,1),(27,'Oficion Contraparte institucional','oficion_contraparte_institucional.pdf','PDF',NULL,7);
/*!40000 ALTER TABLE `elemento` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `elemento_proyecto`
--

DROP TABLE IF EXISTS `elemento_proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `elemento_proyecto` (
  `id_elementos_proyecto` int NOT NULL AUTO_INCREMENT,
  `informacion` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` date NOT NULL,
  `id_elemento_fk` int NOT NULL,
  `id_etapa_proyecto_fk` int NOT NULL,
  PRIMARY KEY (`id_elementos_proyecto`),
  KEY `fk_ELEMENTO_has_ETAPAS_PROYECTO_ETAPAS_PROYECTO1_idx` (`id_etapa_proyecto_fk`),
  KEY `fk_ELEMENTO_has_ETAPAS_PROYECTO_ELEMENTO1_idx` (`id_elemento_fk`),
  CONSTRAINT `fk_ELEMENTO_has_ETAPAS_PROYECTO_ELEMENTO1` FOREIGN KEY (`id_elemento_fk`) REFERENCES `elemento` (`id_elemento`),
  CONSTRAINT `fk_ELEMENTO_has_ETAPAS_PROYECTO_ETAPAS_PROYECTO1` FOREIGN KEY (`id_etapa_proyecto_fk`) REFERENCES `etapa_proyecto` (`id_etapa_proyecto`)
) ENGINE=InnoDB AUTO_INCREMENT=1419 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `elemento_proyecto`
--

LOCK TABLES `elemento_proyecto` WRITE;
/*!40000 ALTER TABLE `elemento_proyecto` DISABLE KEYS */;
/*!40000 ALTER TABLE `elemento_proyecto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etapa`
--

DROP TABLE IF EXISTS `etapa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etapa` (
  `id_etapa` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `descripcion` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nota_minima_aprobacion` double DEFAULT NULL,
  `id_rol_fk` int DEFAULT NULL,
  PRIMARY KEY (`id_etapa`),
  KEY `etapa_rol_FK` (`id_rol_fk`),
  CONSTRAINT `etapa_rol_FK` FOREIGN KEY (`id_rol_fk`) REFERENCES `rol` (`id_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etapa`
--

LOCK TABLES `etapa` WRITE;
/*!40000 ALTER TABLE `etapa` DISABLE KEYS */;
INSERT INTO `etapa` VALUES (1,'Creacion Proyecto','Estudiante debera solicitar la revision de su anteproyecto.',NULL,1),(2,'Revision Secretaria','Secretaria debera aprobar la informacion del anteproyecto.',NULL,4),(3,'Revision Supervisor','Supervisor debera aprobar la informacion del anteproyecto y debera asignar el asesor.',NULL,2),(4,'Convocatoria Evaluacion Anteproyecto','Supervisor debe realizar la convocatoria para la evaluacion del anteproyecto por parte de la Comision de EPS.',NULL,2),(5,'Carga Convocatoria Anteproyecto','Coordinador de EPS debe cargar la convocatoria de la evaluacion de antreproyecto firmada.',NULL,6),(6,'Evaluacion Anteproyecto','Supervisor debera registrar el resultado de la evaluacion de anteproyecto.',61,2),(7,'Carga Carta Aceptacion Contraparte','Estudiante debera realizar los cambios solicitados y cargar la carta de aceptacion de la contraparte institucional',NULL,1),(8,'Habilitacion de Bitacora','Supervisor debe asignar el representante de contraparte institucional y habilita la bitacora al estudiante.',NULL,2),(9,'Bitacora','Estudiante debera registrar su bitacora y al finalizar la ejecucion del proyecto debera finalizar la bitacora.',NULL,1),(10,'Aprobacion Bitacora','Supervisor debera aprobar la finalizacion de la bitacora.',NULL,2),(11,'Carga Informe Final','Estudiante debera cargar su informe final y carta de finalizacion de asesor.',NULL,1),(12,'Convocatoria Examen General','Supervisor debe realizar la convocatoria al examen general de EPS.',NULL,2),(13,'Carga Convocatoria Examen General','Coordinador de Eps deber cargar la convocatoria del examen general firmada.',NULL,6),(14,'Evaluacion Examen General','Supervisor debera registrar el resultado del examen general de EPS.',61,2),(15,'Correcciones de Informe y Redaccion de Articulo','Estudiante debe realizar correcciones al Informe final y cargar articulo cientifico',NULL,1),(16,'Revision Informe Final','Supervisor revisa y debe aprobar el informe final',NULL,2),(17,'Dictamen de Revision','Supervisor debe cargar dictamen y carta de revision',NULL,2),(18,'Acta de Finalizacion','Coordinador de Eps debe crear el acta de finalizacion.',NULL,6),(19,'Finalizado','Proyecto de EPS finalizado',NULL,NULL);
/*!40000 ALTER TABLE `etapa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etapa_proyecto`
--

DROP TABLE IF EXISTS `etapa_proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etapa_proyecto` (
  `id_etapa_proyecto` int NOT NULL AUTO_INCREMENT,
  `activo` tinyint(1) NOT NULL DEFAULT '0',
  `editable` tinyint(1) NOT NULL,
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date DEFAULT NULL,
  `id_proyecto_fk` int NOT NULL,
  `id_etapa_fk` int NOT NULL,
  PRIMARY KEY (`id_etapa_proyecto`),
  KEY `fk_PROYECTO_EPS_has_ETAPA_ETAPA1_idx` (`id_etapa_fk`),
  KEY `fk_PROYECTO_EPS_has_ETAPA_PROYECTO_EPS1_idx` (`id_proyecto_fk`),
  CONSTRAINT `fk_PROYECTO_EPS_has_ETAPA_ETAPA1` FOREIGN KEY (`id_etapa_fk`) REFERENCES `etapa` (`id_etapa`),
  CONSTRAINT `fk_PROYECTO_EPS_has_ETAPA_PROYECTO_EPS1` FOREIGN KEY (`id_proyecto_fk`) REFERENCES `proyecto` (`id_proyecto`)
) ENGINE=InnoDB AUTO_INCREMENT=546 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etapa_proyecto`
--

LOCK TABLES `etapa_proyecto` WRITE;
/*!40000 ALTER TABLE `etapa_proyecto` DISABLE KEYS */;
/*!40000 ALTER TABLE `etapa_proyecto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `institucion`
--

DROP TABLE IF EXISTS `institucion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `institucion` (
  `id_institucion` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) NOT NULL,
  `direccion` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `direccion_proyecto` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `coordenada_proyecto` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_municipio_fk` int NOT NULL,
  `id_municipio_proyecto_fk` int DEFAULT NULL,
  PRIMARY KEY (`id_institucion`),
  KEY `institucion_FK` (`id_municipio_fk`),
  KEY `institucion_municipio_FK` (`id_municipio_proyecto_fk`),
  CONSTRAINT `institucion_FK` FOREIGN KEY (`id_municipio_fk`) REFERENCES `municipio` (`id_municipio`),
  CONSTRAINT `institucion_municipio_FK` FOREIGN KEY (`id_municipio_proyecto_fk`) REFERENCES `municipio` (`id_municipio`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `institucion`
--

LOCK TABLES `institucion` WRITE;
/*!40000 ALTER TABLE `institucion` DISABLE KEYS */;
/*!40000 ALTER TABLE `institucion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `municipio`
--

DROP TABLE IF EXISTS `municipio`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `municipio` (
  `id_municipio` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `id_departamento_fk` int NOT NULL,
  PRIMARY KEY (`id_municipio`),
  KEY `municipio_FK` (`id_departamento_fk`),
  CONSTRAINT `municipio_FK` FOREIGN KEY (`id_departamento_fk`) REFERENCES `departamento` (`id_departamento`)
) ENGINE=InnoDB AUTO_INCREMENT=342 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `municipio`
--

LOCK TABLES `municipio` WRITE;
/*!40000 ALTER TABLE `municipio` DISABLE KEYS */;
INSERT INTO `municipio` VALUES (4,'Chahal',1),(5,'Chisec',1),(6,'Cobán',1),(7,'Fray Bartolomé de las Casas',1),(8,'La Tinta',1),(9,'Lanquín',1),(10,'Panzós',1),(11,'Raxruhá',1),(12,'San Cristóbal Verapaz',1),(13,'San Juan Chamelco',1),(14,'San Pedro Carchá',1),(15,'Santa Cruz Verapaz',1),(16,'Santa María Cahabón',1),(17,'Senahú',1),(18,'Tamahú',1),(19,'Tactic',1),(20,'Tucurú',1),(21,'Cubulco',2),(22,'Granados',2),(23,'Purulhá',2),(24,'Rabinal',2),(25,'Salamá',2),(26,'San Jerónimo',2),(27,'San Miguel Chicaj',2),(28,'Santa Cruz el Chol',2),(29,'Acatenango',3),(30,'Chimaltenango',3),(31,'El Tejar',3),(32,'Parramos',3),(33,'Patzicía',3),(34,'Patzún',3),(35,'Pochuta',3),(36,'San Andrés Itzapa',3),(37,'San José Poaquíl',3),(38,'San Juan Comalapa',3),(39,'San Martín Jilotepeque',3),(40,'Santa Apolonia',3),(41,'Santa Cruz Balanyá',3),(42,'Tecpán',3),(43,'Yepocapa',3),(44,'Zaragoza',3),(45,'Camotán',4),(46,'Chiquimula',4),(47,'Concepción Las Minas',4),(48,'Esquipulas',4),(49,'Ipala',4),(50,'Jocotán',4),(51,'Olopa',4),(52,'Quezaltepeque',4),(53,'San Jacinto',4),(54,'San José la Arada',4),(55,'San Juan Ermita',4),(56,'El Jícaro',5),(57,'Guastatoya',5),(58,'Morazán',5),(59,'San Agustín Acasaguastlán',5),(60,'San Antonio La Paz',5),(61,'San Cristóbal Acasaguastlán',5),(62,'Sanarate',5),(63,'Sansare',5),(64,'Escuintla',6),(65,'Guanagazapa',6),(66,'Iztapa',6),(67,'La Democracia',6),(68,'La Gomera',6),(69,'Masagua',6),(70,'Nueva Concepción',6),(71,'Palín',6),(72,'San José',6),(73,'San Vicente Pacaya',6),(74,'Santa Lucía Cotzumalguapa',6),(75,'Siquinalá',6),(76,'Tiquisate',6),(77,'Amatitlán',7),(78,'Chinautla',7),(79,'Chuarrancho',7),(80,'Guatemala',7),(81,'Fraijanes',7),(82,'Mixco',7),(83,'Palencia',7),(84,'San José del Golfo',7),(85,'San José Pinula',7),(86,'San Juan Sacatepéquez',7),(87,'San Miguel Petapa',7),(88,'San Pedro Ayampuc',7),(89,'San Pedro Sacatepéquez',7),(90,'San Raymundo',7),(91,'Santa Catarina Pinula',7),(92,'Villa Canales',7),(93,'Villa Nueva',7),(94,'Aguacatán',8),(95,'Chiantla',8),(96,'Colotenango',8),(97,'Concepción Huista',8),(98,'Cuilco',8),(99,'Huehuetenango',8),(100,'Jacaltenango',8),(101,'La Democracia',8),(102,'La Libertad',8),(103,'Malacatancito',8),(104,'Nentón',8),(105,'San Antonio Huista',8),(106,'San Gaspar Ixchil',8),(107,'San Ildefonso Ixtahuacán',8),(108,'San Juan Atitán',8),(109,'San Juan Ixcoy',8),(110,'San Mateo Ixtatán',8),(111,'San Miguel Acatán',8),(112,'San Pedro Nécta',8),(113,'San Pedro Soloma',8),(114,'San Rafael La Independencia',8),(115,'San Rafael Pétzal',8),(116,'San Sebastián Coatán',8),(117,'San Sebastián Huehuetenango',8),(118,'Santa Ana Huista',8),(119,'Santa Bárbara',8),(120,'Santa Cruz Barillas',8),(121,'Santa Eulalia',8),(122,'Santiago Chimaltenango',8),(123,'Tectitán',8),(124,'Todos Santos Cuchumatán',8),(125,'Unión Cantinil',8),(126,'El Estor',9),(127,'Livingston',9),(128,'Los Amates',9),(129,'Morales',9),(130,'Puerto Barrios',9),(131,'Jalapa',10),(132,'Mataquescuintla',10),(133,'Monjas',10),(134,'San Carlos Alzatate',10),(135,'San Luis Jilotepeque',10),(136,'San Manuel Chaparrón',10),(137,'San Pedro Pinula',10),(138,'Agua Blanca',11),(139,'Asunción Mita',11),(140,'Atescatempa',11),(141,'Comapa',11),(142,'Conguaco',11),(143,'El Adelanto',11),(144,'El Progreso',11),(145,'Jalpatagua',11),(146,'Jerez',11),(147,'Jutiapa',11),(148,'Moyuta',11),(149,'Pasaco',11),(150,'Quesada',11),(151,'San José Acatempa',11),(152,'Santa Catarina Mita',11),(153,'Yupiltepeque',11),(154,'Zapotitlán',11),(155,'Dolores',12),(156,'El Chal',12),(157,'Ciudad Flores',12),(158,'La Libertad',12),(159,'Las Cruces',12),(160,'Melchor de Mencos',12),(161,'Poptún',12),(162,'San Andrés',12),(163,'San Benito',12),(164,'San Francisco',12),(165,'San José',12),(166,'San Luis',12),(167,'Santa Ana',12),(168,'Sayaxché',12),(169,'Almolonga',13),(170,'Cabricán',13),(171,'Cajolá',13),(172,'Cantel',13),(173,'Coatepeque',13),(174,'Colomba Costa Cuca',13),(175,'Concepción Chiquirichapa',13),(176,'El Palmar',13),(177,'Flores Costa Cuca',13),(178,'Génova',13),(179,'Huitán',13),(180,'La Esperanza',13),(181,'Olintepeque',13),(182,'Palestina de Los Altos',13),(183,'Quetzaltenango',13),(184,'Salcajá',13),(185,'San Carlos Sija',13),(186,'San Francisco La Unión',13),(187,'San Juan Ostuncalco',13),(188,'San Martín Sacatepéquez',13),(189,'San Mateo',13),(190,'San Miguel Sigüilá',13),(191,'Sibilia',13),(192,'Zunil',13),(193,'Canillá',14),(194,'Chajul',14),(195,'Chicamán',14),(196,'Chiché',14),(197,'Chichicastenango',14),(198,'Chinique',14),(199,'Cunén',14),(200,'Ixcán Playa Grande',14),(201,'Joyabaj',14),(202,'Nebaj',14),(203,'Pachalum',14),(204,'Patzité',14),(205,'Sacapulas',14),(206,'San Andrés Sajcabajá',14),(207,'San Antonio Ilotenango',14),(208,'San Bartolomé Jocotenango',14),(209,'San Juan Cotzal',14),(210,'San Pedro Jocopilas',14),(211,'Santa Cruz del Quiché',14),(212,'Uspantán',14),(213,'Zacualpa',14),(214,'Champerico',15),(215,'El Asintal',15),(216,'Nuevo San Carlos',15),(217,'Retalhuleu',15),(218,'San Andrés Villa Seca',15),(219,'San Felipe Reu',15),(220,'San Martín Zapotitlán',15),(221,'San Sebastián',15),(222,'Santa Cruz Muluá',15),(223,'Alotenango',16),(224,'Ciudad Vieja',16),(225,'Jocotenango',16),(226,'Antigua Guatemala',16),(227,'Magdalena Milpas Altas',16),(228,'Pastores',16),(229,'San Antonio Aguas Calientes',16),(230,'San Bartolomé Milpas Altas',16),(231,'San Lucas Sacatepéquez',16),(232,'San Miguel Dueñas',16),(233,'Santa Catarina Barahona',16),(234,'Santa Lucía Milpas Altas',16),(235,'Santa María de Jesús',16),(236,'Santiago Sacatepéquez',16),(237,'Santo Domingo Xenacoj',16),(238,'Sumpango',16),(239,'Ayutla',17),(240,'Catarina',17),(241,'Comitancillo',17),(242,'Concepción Tutuapa',17),(243,'El Quetzal',17),(244,'El Tumbador',17),(245,'Esquipulas Palo Gordo',17),(246,'Ixchiguán',17),(247,'La Blanca',17),(248,'La Reforma',17),(249,'Malacatán',17),(250,'Nuevo Progreso',17),(251,'Ocós',17),(252,'Pajapita',17),(253,'Río Blanco',17),(254,'San Antonio Sacatepéquez',17),(255,'San Cristóbal Cucho',17),(256,'San José El Rodeo',17),(257,'San José Ojetenam',17),(258,'San Lorenzo',17),(259,'San Marcos',17),(260,'San Miguel Ixtahuacán',17),(261,'San Pablo',17),(262,'San Pedro Sacatepéquez',17),(263,'San Rafael Pie de la Cuesta',17),(264,'Sibinal',17),(265,'Sipacapa',17),(266,'Tacaná',17),(267,'Tajumulco',17),(268,'Tejutla',17),(269,'Barberena',18),(270,'Casillas',18),(271,'Chiquimulilla',18),(272,'Cuilapa',18),(273,'Guazacapán',18),(274,'Nueva Santa Rosa',18),(275,'Oratorio',18),(276,'Pueblo Nuevo Viñas',18),(277,'San Juan Tecuaco',18),(278,'San Rafael las Flores',18),(279,'Santa Cruz Naranjo',18),(280,'Santa María Ixhuatán',18),(281,'Santa Rosa de Lima',18),(282,'Taxisco',18),(283,'Concepción',19),(284,'Nahualá',19),(285,'Panajachel',19),(286,'San Andrés Semetabaj',19),(287,'San Antonio Palopó',19),(288,'San José Chacayá',19),(289,'San Juan La Laguna',19),(290,'San Lucas Tolimán',19),(291,'San Marcos La Laguna',19),(292,'San Pablo La Laguna',19),(293,'San Pedro La Laguna',19),(294,'Santa Catarina Ixtahuacán',19),(295,'Santa Catarina Palopó',19),(296,'Santa Clara La Laguna',19),(297,'Santa Cruz La Laguna',19),(298,'Santa Lucía Utatlán',19),(299,'Santa María Visitación',19),(300,'Santiago Atitlán',19),(301,'Sololá',19),(302,'Chicacao',20),(303,'Cuyotenango',20),(304,'Mazatenango',20),(305,'Patulul',20),(306,'Pueblo Nuevo',20),(307,'Río Bravo',20),(308,'Samayac',20),(309,'San Antonio Suchitepéquez',20),(310,'San Bernardino',20),(311,'San Francisco Zapotitlán',20),(312,'San Gabriel',20),(313,'San José El Idolo',20),(314,'San José La Maquina',20),(315,'San Juan Bautista',20),(316,'San Lorenzo',20),(317,'San Miguel Panán',20),(318,'San Pablo Jocopilas',20),(319,'Santa Bárbara',20),(320,'Santo Domingo Suchitepéquez',20),(321,'Santo Tomás La Unión',20),(322,'Zunilito',20),(323,'Momostenango',21),(324,'San Andrés Xecul',21),(325,'San Bartolo',21),(326,'San Cristóbal Totonicapán',21),(327,'San Francisco El Alto',21),(328,'Santa Lucía La Reforma',21),(329,'Santa María Chiquimula',21),(330,'Totonicapán',21),(331,'Cabañas',22),(332,'Estanzuela',22),(333,'Gualán',22),(334,'Huité',22),(335,'La Unión',22),(336,'Río Hondo',22),(337,'San Diego',22),(338,'San Jorge',22),(339,'Teculután',22),(340,'Usumatlán',22),(341,'Zacapa',22);
/*!40000 ALTER TABLE `municipio` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permisos`
--

DROP TABLE IF EXISTS `permisos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permisos` (
  `id_permisos` int NOT NULL,
  `titulo` varchar(45) NOT NULL,
  `estado` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`id_permisos`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permisos`
--

LOCK TABLES `permisos` WRITE;
/*!40000 ALTER TABLE `permisos` DISABLE KEYS */;
/*!40000 ALTER TABLE `permisos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `persona`
--

DROP TABLE IF EXISTS `persona`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `persona` (
  `id_persona` int NOT NULL AUTO_INCREMENT,
  `correo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `nombre_completo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `telefono` varchar(200) NOT NULL,
  `numero_colegiado` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `registro_academico` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `dpi` varchar(100) DEFAULT NULL,
  `direccion` varchar(100) DEFAULT NULL,
  `puesto` varchar(200) DEFAULT NULL,
  `titulo_asesor` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `rol` enum('ASESOR','ASESOR TECNICO','CONTRAPARTE INSTITUCIONAL') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `id_titulo_fk` int DEFAULT NULL,
  `id_proyecto_fk` int NOT NULL,
  PRIMARY KEY (`id_persona`),
  KEY `persona_FK` (`id_proyecto_fk`),
  KEY `persona_FK_1` (`rol`),
  KEY `persona_titulo_FK` (`id_titulo_fk`),
  CONSTRAINT `persona_FK` FOREIGN KEY (`id_proyecto_fk`) REFERENCES `proyecto` (`id_proyecto`),
  CONSTRAINT `persona_titulo_FK` FOREIGN KEY (`id_titulo_fk`) REFERENCES `titulo` (`id_titulo`)
) ENGINE=InnoDB AUTO_INCREMENT=77 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `persona`
--

LOCK TABLES `persona` WRITE;
/*!40000 ALTER TABLE `persona` DISABLE KEYS */;
/*!40000 ALTER TABLE `persona` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `prorroga`
--

DROP TABLE IF EXISTS `prorroga`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `prorroga` (
  `id_prorroga` int NOT NULL AUTO_INCREMENT,
  `link_solicitud` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `link_amparo` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `dias_extension` int NOT NULL,
  `fecha_solicitud` date NOT NULL,
  `comentario_supervisor` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci,
  `aprobado` tinyint(1) DEFAULT NULL,
  `id_proyecto_fk` int NOT NULL,
  PRIMARY KEY (`id_prorroga`),
  KEY `extension_proyecto_FK` (`id_proyecto_fk`),
  CONSTRAINT `extension_proyecto_FK` FOREIGN KEY (`id_proyecto_fk`) REFERENCES `proyecto` (`id_proyecto`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `prorroga`
--

LOCK TABLES `prorroga` WRITE;
/*!40000 ALTER TABLE `prorroga` DISABLE KEYS */;
/*!40000 ALTER TABLE `prorroga` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `proyecto`
--

DROP TABLE IF EXISTS `proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `proyecto` (
  `id_proyecto` int NOT NULL AUTO_INCREMENT,
  `semestre` enum('Primer Semestre','Segundo Semestre') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_inicio` date NOT NULL,
  `fecha_fin` date DEFAULT NULL,
  `resultado` enum('APROBADO','RECHAZADO') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_institucion_fk` int NOT NULL,
  `id_carrera_fk` int NOT NULL,
  `id_usuario_fk` int NOT NULL,
  PRIMARY KEY (`id_proyecto`),
  KEY `proyecto_eps_FK_1` (`id_institucion_fk`),
  KEY `proyecto_eps_FK` (`id_carrera_fk`),
  KEY `proyecto_eps_FK_2` (`id_usuario_fk`),
  CONSTRAINT `proyecto_eps_FK` FOREIGN KEY (`id_carrera_fk`) REFERENCES `carrera` (`id_carrera`),
  CONSTRAINT `proyecto_eps_FK_1` FOREIGN KEY (`id_institucion_fk`) REFERENCES `institucion` (`id_institucion`),
  CONSTRAINT `proyecto_eps_FK_2` FOREIGN KEY (`id_usuario_fk`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `proyecto`
--

LOCK TABLES `proyecto` WRITE;
/*!40000 ALTER TABLE `proyecto` DISABLE KEYS */;
/*!40000 ALTER TABLE `proyecto` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recurso`
--

DROP TABLE IF EXISTS `recurso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recurso` (
  `id_recurso` int NOT NULL AUTO_INCREMENT,
  `descripcion` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `link` varchar(200) NOT NULL,
  `tipo_recurso` enum('IMAGEN','PDF','LINK','INFORME MENSUAL','OTROS') CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `fecha` date NOT NULL,
  `id_bitacora_fk` int NOT NULL,
  PRIMARY KEY (`id_recurso`),
  KEY `recurso_FK` (`id_bitacora_fk`),
  CONSTRAINT `recurso_FK` FOREIGN KEY (`id_bitacora_fk`) REFERENCES `bitacora` (`id_bitacora`)
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recurso`
--

LOCK TABLES `recurso` WRITE;
/*!40000 ALTER TABLE `recurso` DISABLE KEYS */;
/*!40000 ALTER TABLE `recurso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `refresh_token`
--

DROP TABLE IF EXISTS `refresh_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `refresh_token` (
  `id_refresh_token` int NOT NULL AUTO_INCREMENT,
  `token` varchar(200) NOT NULL,
  `expiry_date` timestamp NOT NULL,
  `id_usuario_fk` int NOT NULL,
  PRIMARY KEY (`id_refresh_token`),
  KEY `refresh_token_FK` (`id_usuario_fk`),
  CONSTRAINT `refresh_token_FK` FOREIGN KEY (`id_usuario_fk`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=1590 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `refresh_token`
--

LOCK TABLES `refresh_token` WRITE;
/*!40000 ALTER TABLE `refresh_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `refresh_token` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol` (
  `id_rol` int NOT NULL,
  `titulo` varchar(45) NOT NULL,
  `contiene_carrera` tinyint NOT NULL,
  `contiene_registro` tinyint NOT NULL,
  `contiene_colegiado` tinyint NOT NULL,
  `contiene_titulo` tinyint NOT NULL,
  `contiene_multiples_carreras` tinyint(1) NOT NULL,
  PRIMARY KEY (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` VALUES (1,'Estudiante',1,1,0,0,1),(2,'Supervisor',1,1,1,1,0),(3,'Asesor',1,0,1,1,1),(4,'Secretaria',0,1,0,1,0),(5,'Coordinador Carrera',1,1,1,1,0),(6,'Coordinador EPS',0,1,1,1,0),(7,'Contraparte Institucional',0,0,0,1,0);
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol_permisos`
--

DROP TABLE IF EXISTS `rol_permisos`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol_permisos` (
  `id_rol_fk` int NOT NULL,
  `id_permiso_fk` int NOT NULL,
  PRIMARY KEY (`id_rol_fk`,`id_permiso_fk`),
  KEY `fk_ROL_has_PERMISOS_PERMISOS1_idx` (`id_permiso_fk`),
  KEY `fk_ROL_has_PERMISOS_ROL1_idx` (`id_rol_fk`),
  CONSTRAINT `fk_ROL_has_PERMISOS_PERMISOS1` FOREIGN KEY (`id_permiso_fk`) REFERENCES `permisos` (`id_permisos`),
  CONSTRAINT `fk_ROL_has_PERMISOS_ROL1` FOREIGN KEY (`id_rol_fk`) REFERENCES `rol` (`id_rol`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol_permisos`
--

LOCK TABLES `rol_permisos` WRITE;
/*!40000 ALTER TABLE `rol_permisos` DISABLE KEYS */;
/*!40000 ALTER TABLE `rol_permisos` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol_usuario`
--

DROP TABLE IF EXISTS `rol_usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol_usuario` (
  `id_rol_usuario` int NOT NULL AUTO_INCREMENT,
  `id_usuario_fk` int NOT NULL,
  `id_rol_fk` int NOT NULL,
  PRIMARY KEY (`id_rol_usuario`),
  KEY `rol_usuario_FK` (`id_rol_fk`),
  KEY `rol_usuario_FK_1` (`id_usuario_fk`),
  CONSTRAINT `rol_usuario_FK` FOREIGN KEY (`id_rol_fk`) REFERENCES `rol` (`id_rol`),
  CONSTRAINT `rol_usuario_FK_1` FOREIGN KEY (`id_usuario_fk`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol_usuario`
--

LOCK TABLES `rol_usuario` WRITE;
/*!40000 ALTER TABLE `rol_usuario` DISABLE KEYS */;
INSERT INTO `rol_usuario` VALUES (1,1,2),(2,2,1),(3,3,4);
/*!40000 ALTER TABLE `rol_usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `titulo`
--

DROP TABLE IF EXISTS `titulo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `titulo` (
  `id_titulo` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `abreviatura` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id_titulo`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `titulo`
--

LOCK TABLES `titulo` WRITE;
/*!40000 ALTER TABLE `titulo` DISABLE KEYS */;
INSERT INTO `titulo` VALUES (1,'Ingeniero','Ing.'),(2,'Licenciado','Lic.'),(3,'Doctor','Dr.'),(4,'Secretario','Secr.'),(5,'Estudiante','Est.'),(6,'Otro',' ');
/*!40000 ALTER TABLE `titulo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `token_confirmacion`
--

DROP TABLE IF EXISTS `token_confirmacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `token_confirmacion` (
  `id_token_confirmacion` int NOT NULL AUTO_INCREMENT,
  `token` varchar(255) NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `fecha_creacion` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `id_usuario_fk` int NOT NULL,
  PRIMARY KEY (`id_token_confirmacion`),
  KEY `id_usuario_fk` (`id_usuario_fk`),
  CONSTRAINT `token_confirmacion_ibfk_1` FOREIGN KEY (`id_usuario_fk`) REFERENCES `usuario` (`id_usuario`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=75 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `token_confirmacion`
--

LOCK TABLES `token_confirmacion` WRITE;
/*!40000 ALTER TABLE `token_confirmacion` DISABLE KEYS */;
/*!40000 ALTER TABLE `token_confirmacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `correo` varchar(45) NOT NULL,
  `nombre_completo` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `registro_academico` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `numero_colegiado` varchar(30) DEFAULT NULL,
  `dpi` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `direccion` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `telefono` varchar(15) NOT NULL,
  `genero` enum('FEMENINO','MASCULINO') DEFAULT NULL,
  `cuenta_activa` tinyint(1) NOT NULL DEFAULT '0',
  `password` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL,
  `id_titulo_fk` int NOT NULL,
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `CORREO_UNIQUE` (`correo`),
  UNIQUE KEY `DPI_UNIQUE` (`dpi`),
  UNIQUE KEY `REGISTRO_UNIQUE` (`registro_academico`),
  UNIQUE KEY `COLEGIADO_UNIQUE` (`numero_colegiado`),
  KEY `usuario_FK` (`id_titulo_fk`),
  CONSTRAINT `usuario_FK` FOREIGN KEY (`id_titulo_fk`) REFERENCES `titulo` (`id_titulo`)
) ENGINE=InnoDB AUTO_INCREMENT=88 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (1,'jose.granados@cunoc.edu.gt','José Moisés Granados Guevara','201030873','11096','201030873','Quetzaltenango','+502 55906132',NULL,1,'$2a$10$Gma0vAFYxfkq8.wzWjZhjOo/SkCesSHx8XB3HemIdze0qmDUcLYIm',1),(2,'edvinteodoro-gonzalezrafael@cunoc.edu.gt','Edvin Teodoro González Rafael','201630873',NULL,'3218359051323',NULL,'+502 31615293',NULL,1,'$2a$10$Gma0vAFYxfkq8.wzWjZhjOo/SkCesSHx8XB3HemIdze0qmDUcLYIm',5),(3,'secretaria_epsing@cunoc.edu.gt','Magdalena Sierra','201530873',NULL,'201530873',NULL,'+502 12345678',NULL,1,'$2a$10$Gma0vAFYxfkq8.wzWjZhjOo/SkCesSHx8XB3HemIdze0qmDUcLYIm',6);
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario_proyecto`
--

DROP TABLE IF EXISTS `usuario_proyecto`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario_proyecto` (
  `id_usuario_proyecto` int NOT NULL AUTO_INCREMENT,
  `fecha_asignacion` date NOT NULL,
  `fecha_finalizacion` date DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `id_rol_fk` int NOT NULL,
  `id_usuario_fk` int NOT NULL,
  `id_proyecto_fk` int NOT NULL,
  PRIMARY KEY (`id_usuario_proyecto`),
  KEY `usuario_proyecto_FK` (`id_proyecto_fk`),
  KEY `usuario_proyecto_FK_1` (`id_usuario_fk`),
  KEY `usuario_proyecto_FK_2` (`id_rol_fk`),
  CONSTRAINT `usuario_proyecto_FK` FOREIGN KEY (`id_proyecto_fk`) REFERENCES `proyecto` (`id_proyecto`),
  CONSTRAINT `usuario_proyecto_FK_1` FOREIGN KEY (`id_usuario_fk`) REFERENCES `usuario` (`id_usuario`),
  CONSTRAINT `usuario_proyecto_FK_2` FOREIGN KEY (`id_rol_fk`) REFERENCES `rol` (`id_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=123 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario_proyecto`
--

LOCK TABLES `usuario_proyecto` WRITE;
/*!40000 ALTER TABLE `usuario_proyecto` DISABLE KEYS */;
/*!40000 ALTER TABLE `usuario_proyecto` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-26 23:33:33
