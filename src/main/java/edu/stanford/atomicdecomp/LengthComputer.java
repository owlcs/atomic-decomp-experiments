package edu.stanford.atomicdecomp;

import org.semanticweb.owlapi.model.*;

import java.util.Set;

/**
 * @author Matthew Horridge, Stanford University, Bio-Medical Informatics Research Group, Date: 06/05/2014
 */
public class LengthComputer implements OWLObjectVisitorEx<Integer> {

    @Override
    public Integer visit(OWLAnnotation node) {
        return null;
    }

    @Override
    public Integer visit(IRI iri) {
        return 0;
    }

    @Override
    public Integer visit(OWLAnonymousIndividual individual) {
        return 1;
    }

    @Override
    public Integer visit(OWLSubClassOfAxiom axiom) {
        return axiom.getSuperClass().accept(this) + axiom.getSuperClass().accept(this);
    }

    @Override
    public Integer visit(OWLNegativeObjectPropertyAssertionAxiom axiom) {
        return 3;
    }

    @Override
    public Integer visit(OWLAsymmetricObjectPropertyAxiom axiom) {
        return 1;
    }

    @Override
    public Integer visit(OWLReflexiveObjectPropertyAxiom axiom) {
        return 1;
    }

    @Override
    public Integer visit(OWLDisjointClassesAxiom axiom) {
        return getCollection(axiom.getClassExpressions());
    }

    @Override
    public Integer visit(OWLDataPropertyDomainAxiom axiom) {
        return axiom.getDomain().accept(this) + 1;
    }

    @Override
    public Integer visit(OWLObjectPropertyDomainAxiom axiom) {
        return axiom.getDomain().accept(this) + 1;
    }

    @Override
    public Integer visit(OWLEquivalentObjectPropertiesAxiom axiom) {
        return axiom.getProperties().size();
    }

    @Override
    public Integer visit(OWLNegativeDataPropertyAssertionAxiom axiom) {
        return 3;
    }

    @Override
    public Integer visit(OWLDifferentIndividualsAxiom axiom) {
        return axiom.getIndividuals().size();
    }

    @Override
    public Integer visit(OWLDisjointDataPropertiesAxiom axiom) {
        return axiom.getProperties().size();
    }

    @Override
    public Integer visit(OWLDisjointObjectPropertiesAxiom axiom) {
        return axiom.getProperties().size();
    }

    @Override
    public Integer visit(OWLObjectPropertyRangeAxiom axiom) {
        return axiom.getRange().accept(this) + 1;
    }

    @Override
    public Integer visit(OWLObjectPropertyAssertionAxiom axiom) {
        return 3;
    }

    @Override
    public Integer visit(OWLFunctionalObjectPropertyAxiom axiom) {
        return 1;
    }

    @Override
    public Integer visit(OWLSubObjectPropertyOfAxiom axiom) {
        return 2;
    }

    @Override
    public Integer visit(OWLDisjointUnionAxiom axiom) {
        return axiom.getClassExpressions().size() + 1;
    }

    @Override
    public Integer visit(OWLDeclarationAxiom axiom) {
        return 0;
    }

    @Override
    public Integer visit(OWLAnnotationAssertionAxiom axiom) {
        return 0;
    }

    @Override
    public Integer visit(OWLSymmetricObjectPropertyAxiom axiom) {
        return 1;
    }

    @Override
    public Integer visit(OWLDataPropertyRangeAxiom axiom) {
        return axiom.getRange().accept(this) + 1;
    }

    @Override
    public Integer visit(OWLFunctionalDataPropertyAxiom axiom) {
        return 1;
    }

    @Override
    public Integer visit(OWLEquivalentDataPropertiesAxiom axiom) {
        return axiom.getProperties().size();
    }

    @Override
    public Integer visit(OWLClassAssertionAxiom axiom) {
        return axiom.getClassExpression().accept(this) + 1;
    }

    @Override
    public Integer visit(OWLEquivalentClassesAxiom axiom) {
        Set<OWLClassExpression> ops = axiom.getClassExpressions();
        return getCollection(ops);
    }

    private Integer getCollection(Set<? extends OWLObject> ops) {
        int result = 0;
        for(OWLObject ce : ops) {
            result += ce.accept(this);
        }
        return result;
    }


    @Override
    public Integer visit(OWLDataPropertyAssertionAxiom axiom) {
        return 3;
    }

    @Override
    public Integer visit(OWLTransitiveObjectPropertyAxiom axiom) {
        return 1;
    }

    @Override
    public Integer visit(OWLIrreflexiveObjectPropertyAxiom axiom) {
        return 1;
    }

    @Override
    public Integer visit(OWLSubDataPropertyOfAxiom axiom) {
        return 2;
    }

    @Override
    public Integer visit(OWLInverseFunctionalObjectPropertyAxiom axiom) {
        return 1;
    }

    @Override
    public Integer visit(OWLSameIndividualAxiom axiom) {
        return axiom.getIndividuals().size();
    }

    @Override
    public Integer visit(OWLSubPropertyChainOfAxiom axiom) {
        return 2;
    }

    @Override
    public Integer visit(OWLInverseObjectPropertiesAxiom axiom) {
        return 2;
    }

    @Override
    public Integer visit(OWLHasKeyAxiom axiom) {
        return 0;
    }

    @Override
    public Integer visit(OWLDatatypeDefinitionAxiom axiom) {
        return 0;
    }

    @Override
    public Integer visit(SWRLRule rule) {
        return 0;
    }

    @Override
    public Integer visit(OWLSubAnnotationPropertyOfAxiom axiom) {
        return 0;
    }

    @Override
    public Integer visit(OWLAnnotationPropertyDomainAxiom axiom) {
        return 0;
    }

    @Override
    public Integer visit(OWLAnnotationPropertyRangeAxiom axiom) {
        return 0;
    }

    @Override
    public Integer visit(OWLClass ce) {
        return 1;
    }

    @Override
    public Integer visit(OWLObjectIntersectionOf ce) {
        return getCollection(ce.getOperands());
    }

    @Override
    public Integer visit(OWLObjectUnionOf ce) {
        return getCollection(ce.getOperands());
    }

    @Override
    public Integer visit(OWLObjectComplementOf ce) {
        return ce.getOperand().accept(this);
    }

    @Override
    public Integer visit(OWLObjectSomeValuesFrom ce) {
        return getRestrictionsSize(ce);
    }

    private int getRestrictionsSize(HasFiller<?> ce) {
        return 1 + ce.getFiller().accept(this);
    }

    @Override
    public Integer visit(OWLObjectAllValuesFrom ce) {
        return getRestrictionsSize(ce);
    }

    @Override
    public Integer visit(OWLObjectHasValue ce) {
        return getRestrictionsSize(ce);
    }

    @Override
    public Integer visit(OWLObjectMinCardinality ce) {
        return getRestrictionsSize(ce);
    }

    @Override
    public Integer visit(OWLObjectExactCardinality ce) {
        return getRestrictionsSize(ce);
    }

    @Override
    public Integer visit(OWLObjectMaxCardinality ce) {
        return getRestrictionsSize(ce);
    }

    @Override
    public Integer visit(OWLObjectHasSelf ce) {
        return 1;
    }

    @Override
    public Integer visit(OWLObjectOneOf ce) {
        return ce.getIndividuals().size();
    }

    @Override
    public Integer visit(OWLDataSomeValuesFrom ce) {
        return getRestrictionsSize(ce);
    }

    @Override
    public Integer visit(OWLDataAllValuesFrom ce) {
        return getRestrictionsSize(ce);
    }

    @Override
    public Integer visit(OWLDataHasValue ce) {
        return getRestrictionsSize(ce);
    }

    @Override
    public Integer visit(OWLDataMinCardinality ce) {
        return getRestrictionsSize(ce);
    }

    @Override
    public Integer visit(OWLDataExactCardinality ce) {
        return getRestrictionsSize(ce);
    }

    @Override
    public Integer visit(OWLDataMaxCardinality ce) {
        return getRestrictionsSize(ce);
    }

    @Override
    public Integer visit(OWLDatatype node) {
        return 1;
    }

    @Override
    public Integer visit(OWLDataComplementOf node) {
        return node.getDataRange().accept(this);
    }

    @Override
    public Integer visit(OWLDataOneOf node) {
        return node.getValues().size();
    }

    @Override
    public Integer visit(OWLDataIntersectionOf node) {
        return getCollection(node.getOperands());
    }

    @Override
    public Integer visit(OWLDataUnionOf node) {
        return getCollection(node.getOperands());
    }

    @Override
    public Integer visit(OWLDatatypeRestriction node) {
        return 1;
    }

    @Override
    public Integer visit(OWLLiteral node) {
        return 1;
    }

    @Override
    public Integer visit(OWLFacetRestriction node) {
        return 0;
    }

    @Override
    public Integer visit(OWLNamedIndividual individual) {
        return 1;
    }

    @Override
    public Integer visit(OWLAnnotationProperty property) {
        return 0;
    }

    @Override
    public Integer visit(OWLOntology ontology) {
        return getCollection(ontology.getLogicalAxioms());
    }

    @Override
    public Integer visit(OWLObjectProperty property) {
        return 1;
    }

    @Override
    public Integer visit(OWLObjectInverseOf property) {
        return 1;
    }

    @Override
    public Integer visit(OWLDataProperty property) {
        return 1;
    }

    @Override
    public Integer visit(SWRLClassAtom node) {
        return 0;
    }

    @Override
    public Integer visit(SWRLDataRangeAtom node) {
        return 0;
    }

    @Override
    public Integer visit(SWRLObjectPropertyAtom node) {
        return 0;
    }

    @Override
    public Integer visit(SWRLDataPropertyAtom node) {
        return 0;
    }

    @Override
    public Integer visit(SWRLBuiltInAtom node) {
        return 0;
    }

    @Override
    public Integer visit(SWRLVariable node) {
        return 0;
    }

    @Override
    public Integer visit(SWRLIndividualArgument node) {
        return 0;
    }

    @Override
    public Integer visit(SWRLLiteralArgument node) {
        return 0;
    }

    @Override
    public Integer visit(SWRLSameIndividualAtom node) {
        return 0;
    }

    @Override
    public Integer visit(SWRLDifferentIndividualsAtom node) {
        return 0;
    }
}
